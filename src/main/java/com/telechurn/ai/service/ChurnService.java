package com.telechurn.ai.service;

import com.telechurn.ai.entity.Customer;
import com.telechurn.ai.repository.CustomerRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class ChurnService {

    private final CustomerRepository customerRepository;

    public ChurnService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Map<String, Double> churnByContract() {
        return churnRateByField(List.of("Month-to-month", "One year", "Two year"), Customer::getContract);
    }

    public Map<String, Double> churnByInternetService() {
        return churnRateByField(List.of("DSL", "Fiber optic", "No"), Customer::getInternetService);
    }

    public Map<String, Double> churnByPaymentMethod() {
        return churnRateByField(List.of("Electronic check", "Mailed check", "Bank transfer (automatic)", "Credit card (automatic)"), Customer::getPaymentMethod);
    }

    public Map<String, Double> churnByTenureBand() {
        return bandRates("0-12", "12-24", "24-48", "48+", customer -> {
            int tenure = customer.getTenure() == null ? 0 : customer.getTenure();
            if (tenure < 12) {
                return "0-12";
            }
            if (tenure < 24) {
                return "12-24";
            }
            if (tenure < 48) {
                return "24-48";
            }
            return "48+";
        });
    }

    public Map<String, Double> churnByMonthlyChargeBand() {
        return bandRates("0-40", "40-70", "70-100", "100+", customer -> {
            double charges = customer.getMonthlyCharges() == null ? 0.0 : customer.getMonthlyCharges();
            if (charges < 40) {
                return "0-40";
            }
            if (charges < 70) {
                return "40-70";
            }
            if (charges < 100) {
                return "70-100";
            }
            return "100+";
        });
    }

    public List<String> insights() {
        List<Customer> customers = customerRepository.findAll();
        long total = customers.size();
        long monthToMonthChurn = customers.stream().filter(customer -> "Month-to-month".equals(customer.getContract()) && "Yes".equals(customer.getChurn())).count();
        long monthToMonthTotal = customers.stream().filter(customer -> "Month-to-month".equals(customer.getContract())).count();
        long noSupportChurn = customers.stream().filter(customer -> "No".equals(customer.getTechSupport()) && "Yes".equals(customer.getChurn())).count();
        long noSupportTotal = customers.stream().filter(customer -> "No".equals(customer.getTechSupport())).count();
        double contractRate = monthToMonthTotal == 0 ? 0.0 : monthToMonthChurn * 100.0 / monthToMonthTotal;
        double supportRate = noSupportTotal == 0 ? 0.0 : noSupportChurn * 100.0 / noSupportTotal;
        return List.of(
                String.format("Month-to-month customers show a churn rate of %.2f%% in this dataset.", contractRate),
                String.format("Customers without technical support show a churn rate of %.2f%%.", supportRate),
                String.format("Overall churn rate across %d customers is %.2f%%.", total, overallChurnRate())
        );
    }

    public Map<String, Long> topDrivers() {
        List<Customer> customers = customerRepository.findAll();
        Map<String, Long> drivers = new LinkedHashMap<>();
        drivers.put("Contract Type", customers.stream().filter(customer -> "Month-to-month".equals(customer.getContract()) && "Yes".equals(customer.getChurn())).count());
        drivers.put("Tenure", customers.stream().filter(customer -> (customer.getTenure() == null ? 0 : customer.getTenure()) < 12 && "Yes".equals(customer.getChurn())).count());
        drivers.put("Monthly Charges", customers.stream().filter(customer -> (customer.getMonthlyCharges() == null ? 0.0 : customer.getMonthlyCharges()) > 70 && "Yes".equals(customer.getChurn())).count());
        drivers.put("Tech Support", customers.stream().filter(customer -> "No".equals(customer.getTechSupport()) && "Yes".equals(customer.getChurn())).count());
        drivers.put("Internet Service", customers.stream().filter(customer -> "Fiber optic".equals(customer.getInternetService()) && "Yes".equals(customer.getChurn())).count());
        return drivers;
    }

    public double overallChurnRate() {
        long total = customerRepository.count();
        return total == 0 ? 0.0 : (customerRepository.countByChurn("Yes") * 100.0 / total);
    }

    private Map<String, Double> churnRateByField(List<String> categories, java.util.function.Function<Customer, String> extractor) {
        Map<String, Double> result = new LinkedHashMap<>();
        List<Customer> customers = customerRepository.findAll();
        for (String category : categories) {
            long total = customers.stream().filter(customer -> category.equals(extractor.apply(customer))).count();
            long churned = customers.stream().filter(customer -> category.equals(extractor.apply(customer)) && "Yes".equals(customer.getChurn())).count();
            result.put(category, total == 0 ? 0.0 : churned * 100.0 / total);
        }
        return result;
    }

    private Map<String, Double> bandRates(String first, String second, String third, String fourth, Function<Customer, String> bander) {
        Map<String, Double> result = new LinkedHashMap<>();
        List<Customer> customers = customerRepository.findAll();
        for (String band : List.of(first, second, third, fourth)) {
            long total = customers.stream().filter(customer -> band.equals(bander.apply(customer))).count();
            long churned = customers.stream().filter(customer -> band.equals(bander.apply(customer)) && "Yes".equals(customer.getChurn())).count();
            result.put(band, total == 0 ? 0.0 : churned * 100.0 / total);
        }
        return result;
    }
}
