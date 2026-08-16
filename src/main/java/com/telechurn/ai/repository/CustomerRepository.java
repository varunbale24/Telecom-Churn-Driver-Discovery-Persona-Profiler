package com.telechurn.ai.repository;

import com.telechurn.ai.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerId(String customerId);

    long countByChurn(String churn);

    long countByRiskLevel(String riskLevel);

    List<Customer> findByChurn(String churn);
}
