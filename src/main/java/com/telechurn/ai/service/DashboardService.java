package com.telechurn.ai.service;

import com.telechurn.ai.repository.CustomerRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final CustomerRepository customerRepository;

    public DashboardService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Map<String, Object> summary() {
        long total = customerRepository.count();
        long churned = customerRepository.countByChurn("Yes");
        long highRisk = customerRepository.countByRiskLevel("HIGH");
        double churnRate = total == 0 ? 0.0 : (churned * 100.0 / total);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalCustomers", total);
        data.put("churnedCustomers", churned);
        data.put("churnRate", churnRate);
        data.put("highRiskCustomers", highRisk);
        return data;
    }
}
