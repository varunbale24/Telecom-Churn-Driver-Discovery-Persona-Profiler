package com.telechurn.ai.service;

import com.telechurn.ai.entity.Customer;
import com.telechurn.ai.entity.Persona;
import com.telechurn.ai.repository.CustomerRepository;
import com.telechurn.ai.repository.PersonaRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonaService {

    private final CustomerRepository customerRepository;
    private final PersonaRepository personaRepository;

    public PersonaService(CustomerRepository customerRepository, PersonaRepository personaRepository) {
        this.customerRepository = customerRepository;
        this.personaRepository = personaRepository;
    }

    public List<Persona> allPersonas() {
        return personaRepository.findAll();
    }

    public Map<String, Object> personaMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();
        for (Persona persona : personaRepository.findAll()) {
            List<Customer> customers = customerRepository.findAll().stream()
                    .filter(customer -> persona.getName().equals(customer.getPersonaName()))
                    .toList();
            long churned = customers.stream().filter(customer -> "Yes".equals(customer.getChurn())).count();
            double churnRate = customers.isEmpty() ? 0.0 : churned * 100.0 / customers.size();
            double averageTenure = customers.stream().mapToInt(Customer::getTenure).average().orElse(0.0);
            double averageCharges = customers.stream().mapToDouble(Customer::getMonthlyCharges).average().orElse(0.0);
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("count", customers.size());
            entry.put("churnRate", churnRate);
            entry.put("averageTenure", averageTenure);
            entry.put("averageMonthlyCharges", averageCharges);
            metrics.put(persona.getName(), entry);
        }
        return metrics;
    }

    @Transactional
    public void assignPersonas() {
        List<Customer> customers = customerRepository.findAll();
        List<Persona> personas = personaRepository.findAll();
        Persona loyal = personas.stream().filter(p -> p.getName().equals("Loyal Customers")).findFirst().orElse(null);
        Persona newAtRisk = personas.stream().filter(p -> p.getName().equals("New / At-Risk Customers")).findFirst().orElse(null);
        Persona premium = personas.stream().filter(p -> p.getName().equals("Premium Customers")).findFirst().orElse(null);
        Persona priceSensitive = personas.stream().filter(p -> p.getName().equals("Price-Sensitive Customers")).findFirst().orElse(null);
        for (Customer customer : customers) {
            if (customer.getTenure() != null && customer.getTenure() >= 36 && customer.getMonthlyCharges() < 75 && loyal != null) {
                customer.setPersona(loyal);
                customer.setPersonaName(loyal.getName());
            } else if (customer.getTenure() != null && customer.getTenure() < 12 && customer.getMonthlyCharges() > 70 && newAtRisk != null) {
                customer.setPersona(newAtRisk);
                customer.setPersonaName(newAtRisk.getName());
            } else if (customer.getMonthlyCharges() != null && customer.getMonthlyCharges() >= 80 && premium != null) {
                customer.setPersona(premium);
                customer.setPersonaName(premium.getName());
            } else if (priceSensitive != null) {
                customer.setPersona(priceSensitive);
                customer.setPersonaName(priceSensitive.getName());
            }
            if (customer.getTenure() != null && customer.getTenure() < 12 || "Month-to-month".equals(customer.getContract())) {
                customer.setRiskLevel("HIGH");
            } else if (customer.getTenure() != null && customer.getTenure() < 24) {
                customer.setRiskLevel("MEDIUM");
            } else {
                customer.setRiskLevel("LOW");
            }
        }
        customerRepository.saveAll(customers);
    }
}
