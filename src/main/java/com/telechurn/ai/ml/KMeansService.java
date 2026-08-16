package com.telechurn.ai.ml;

import com.telechurn.ai.entity.Customer;
import com.telechurn.ai.entity.Persona;
import com.telechurn.ai.repository.CustomerRepository;
import com.telechurn.ai.repository.PersonaRepository;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

@Service
public class KMeansService {

    private final CustomerRepository customerRepository;
    private final PersonaRepository personaRepository;
    private final ModelManager modelManager;
    private SimpleKMeans clusterer;

    public KMeansService(CustomerRepository customerRepository, PersonaRepository personaRepository, ModelManager modelManager) {
        this.customerRepository = customerRepository;
        this.personaRepository = personaRepository;
        this.modelManager = modelManager;
    }

    public synchronized void trainIfNeeded() {
        if (clusterer != null) {
            return;
        }
        if (modelManager.kmeansFile().exists()) {
            load();
            return;
        }
        train();
    }

    public synchronized void train() {
        try {
            Instances data = clusterData(customerRepository.findAll());
            SimpleKMeans kMeans = new SimpleKMeans();
            kMeans.setNumClusters(Math.min(4, Math.max(2, data.numInstances())));
            kMeans.buildClusterer(data);
            this.clusterer = kMeans;
            modelManager.ensureDirectory();
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(modelManager.kmeansFile()))) {
                outputStream.writeObject(kMeans);
            }
            assignPersonasFromClusters(data, kMeans);
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to train K-Means", exception);
        }
    }

    public synchronized void load() {
        try (var inputStream = new java.io.ObjectInputStream(java.nio.file.Files.newInputStream(modelManager.kmeansFile().toPath()))) {
            this.clusterer = (SimpleKMeans) inputStream.readObject();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to load K-Means model", exception);
        }
    }

    public Map<String, Object> snapshot() {
        try {
            trainIfNeeded();
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("clusters", clusterer.numberOfClusters());
            result.put("distribution", clusterStatistics());
            return result;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to create K-Means snapshot", exception);
        }
    }

    private Instances clusterData(List<Customer> customers) {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("tenure"));
        attributes.add(new Attribute("monthlyCharges"));
        attributes.add(new Attribute("totalCharges"));
        attributes.add(new Attribute("serviceCount"));
        Instances data = new Instances("telechurn_clusters", attributes, customers.size());
        for (Customer customer : customers) {
            DenseInstance instance = new DenseInstance(attributes.size());
            instance.setDataset(data);
            instance.setValue(attributes.get(0), customer.getTenure());
            instance.setValue(attributes.get(1), customer.getMonthlyCharges());
            instance.setValue(attributes.get(2), customer.getTotalCharges());
            instance.setValue(attributes.get(3), serviceCount(customer));
            data.add(instance);
        }
        return data;
    }

    private int serviceCount(Customer customer) {
        int count = 0;
        if ("Yes".equals(customer.getPhoneService())) count++;
        if ("Yes".equals(customer.getOnlineSecurity())) count++;
        if ("Yes".equals(customer.getOnlineBackup())) count++;
        if ("Yes".equals(customer.getDeviceProtection())) count++;
        if ("Yes".equals(customer.getTechSupport())) count++;
        if ("Yes".equals(customer.getStreamingTV())) count++;
        if ("Yes".equals(customer.getStreamingMovies())) count++;
        return count;
    }

    private void assignPersonasFromClusters(Instances data, SimpleKMeans kMeans) {
        List<Persona> personas = personaRepository.findAll();
        List<Customer> customers = customerRepository.findAll();
        try {
            for (int index = 0; index < customers.size(); index++) {
                Customer customer = customers.get(index);
                int cluster = kMeans.clusterInstance(data.instance(index));
                Persona persona = personas.get(cluster % personas.size());
                customer.setPersona(persona);
                customer.setPersonaName(persona.getName());
                customer.setRiskLevel(cluster == 0 ? "HIGH" : cluster == 1 ? "MEDIUM" : "LOW");
            }
            customerRepository.saveAll(customers);
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to assign personas from clusters", exception);
        }
    }

    private Map<String, Object> clusterStatistics() {
        Map<String, Object> stats = new LinkedHashMap<>();
        for (Persona persona : personaRepository.findAll()) {
            long count = customerRepository.findAll().stream().filter(customer -> persona.getName().equals(customer.getPersonaName())).count();
            stats.put(persona.getName(), count);
        }
        return stats;
    }
}
