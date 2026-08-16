package com.telechurn.ai.service;

import com.telechurn.ai.entity.Customer;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final CustomerService customerService;
    private final DashboardService dashboardService;
    private final PersonaService personaService;

    public ReportService(CustomerService customerService, DashboardService dashboardService, PersonaService personaService) {
        this.customerService = customerService;
        this.dashboardService = dashboardService;
        this.personaService = personaService;
    }

    public String buildSummary() {
        var summary = dashboardService.summary();
        return String.format(Locale.US,
                "Total customers: %s, churned customers: %s, churn rate: %.2f%%, high-risk customers: %s",
                summary.get("totalCustomers"), summary.get("churnedCustomers"), summary.get("churnRate"), summary.get("highRiskCustomers"));
    }

    public String buildCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("Customer ID,Tenure,Contract,Internet Service,Monthly Charges,Churn,Risk,Persona\n");
        for (Customer customer : customerService.all()) {
            csv.append(customer.getCustomerId()).append(',')
                    .append(customer.getTenure()).append(',')
                    .append(customer.getContract()).append(',')
                    .append(customer.getInternetService()).append(',')
                    .append(customer.getMonthlyCharges()).append(',')
                    .append(customer.getChurn()).append(',')
                    .append(customer.getRiskLevel()).append(',')
                    .append(customer.getPersonaName()).append('\n');
        }
        return csv.toString();
    }
}
