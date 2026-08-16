package com.telechurn.ai.service;

import com.telechurn.ai.entity.Customer;
import com.telechurn.ai.repository.CustomerRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatasetService {

    private final CustomerRepository customerRepository;
    private final ResourceLoader resourceLoader;
    private final String dataFile;

    public DatasetService(CustomerRepository customerRepository, ResourceLoader resourceLoader, @Value("${app.data-file:classpath:data/telco_churn.csv}") String dataFile) {
        this.customerRepository = customerRepository;
        this.resourceLoader = resourceLoader;
        this.dataFile = dataFile;
    }

    @Transactional
    public void loadIfEmpty() {
        if (customerRepository.count() > 0) {
            return;
        }
        Resource resource = resourceLoader.getResource(dataFile);
        if (!resource.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            if (header == null) {
                return;
            }
            String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (fields.length < 21) {
                    continue;
                }
                Customer customer = new Customer();
                customer.setCustomerId(fields[0]);
                customer.setGender(fields[1]);
                customer.setSeniorCitizen("1".equals(fields[2]));
                customer.setPartner("Yes".equals(fields[3]));
                customer.setDependents("Yes".equals(fields[4]));
                customer.setTenure(parseInt(fields[5]));
                customer.setPhoneService(fields[6]);
                customer.setMultipleLines(fields[7]);
                customer.setInternetService(fields[8]);
                customer.setOnlineSecurity(fields[9]);
                customer.setOnlineBackup(fields[10]);
                customer.setDeviceProtection(fields[11]);
                customer.setTechSupport(fields[12]);
                customer.setStreamingTV(fields[13]);
                customer.setStreamingMovies(fields[14]);
                customer.setContract(fields[15]);
                customer.setPaperlessBilling(fields[16]);
                customer.setPaymentMethod(fields[17]);
                customer.setMonthlyCharges(parseDouble(fields[18]));
                customer.setTotalCharges(parseDouble(fields[19]));
                customer.setChurn(fields[20]);
                customer.setRiskLevel("No".equals(customer.getChurn()) ? "LOW" : "HIGH");
                customer.setPersonaName("Unassigned");
                customer.setChurnProbability("Yes".equals(customer.getChurn()) ? 0.75 : 0.15);
                customerRepository.save(customer);
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to load dataset", exception);
        }
    }

    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value.replace("\"", "").trim());
        } catch (Exception exception) {
            return 0;
        }
    }

    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value.replace("\"", "").trim());
        } catch (Exception exception) {
            return 0.0;
        }
    }
}
