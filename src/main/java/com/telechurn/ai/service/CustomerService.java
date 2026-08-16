package com.telechurn.ai.service;

import com.telechurn.ai.entity.Customer;
import com.telechurn.ai.repository.CustomerRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> all() {
        return customerRepository.findAll();
    }

    public List<Customer> search(String query, String contract, String riskLevel, String churn, String sortField, boolean ascending) {
        List<Customer> customers = new ArrayList<>(customerRepository.findAll());
        if (query != null && !query.isBlank()) {
            String needle = query.toLowerCase();
            customers = customers.stream()
                    .filter(customer -> customer.getCustomerId().toLowerCase().contains(needle)
                            || customer.getPersonaName().toLowerCase().contains(needle)
                            || customer.getInternetService().toLowerCase().contains(needle))
                    .toList();
        }
        if (contract != null && !contract.isBlank()) {
            customers = customers.stream().filter(customer -> contract.equals(customer.getContract())).toList();
        }
        if (riskLevel != null && !riskLevel.isBlank()) {
            customers = customers.stream().filter(customer -> riskLevel.equals(customer.getRiskLevel())).toList();
        }
        if (churn != null && !churn.isBlank()) {
            customers = customers.stream().filter(customer -> churn.equals(customer.getChurn())).toList();
        }

        Comparator<Customer> comparator = comparatorFor(sortField);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return customers.stream().sorted(comparator).collect(Collectors.toList());
    }

    private Comparator<Customer> comparatorFor(String sortField) {
        String field = sortField == null ? "customerId" : sortField;
        return switch (field) {
            case "tenure" -> Comparator.comparing(Customer::getTenure, Comparator.nullsLast(Integer::compareTo));
            case "contract" -> Comparator.comparing(Customer::getContract, Comparator.nullsLast(String::compareToIgnoreCase));
            case "internetService" -> Comparator.comparing(Customer::getInternetService, Comparator.nullsLast(String::compareToIgnoreCase));
            case "monthlyCharges" -> Comparator.comparing(Customer::getMonthlyCharges, Comparator.nullsLast(Double::compareTo));
            case "churn" -> Comparator.comparing(Customer::getChurn, Comparator.nullsLast(String::compareToIgnoreCase));
            case "riskLevel" -> Comparator.comparing(Customer::getRiskLevel, Comparator.nullsLast(String::compareToIgnoreCase));
            case "personaName" -> Comparator.comparing(Customer::getPersonaName, Comparator.nullsLast(String::compareToIgnoreCase));
            default -> Comparator.comparing(Customer::getCustomerId, Comparator.nullsLast(String::compareToIgnoreCase));
        };
    }
}
