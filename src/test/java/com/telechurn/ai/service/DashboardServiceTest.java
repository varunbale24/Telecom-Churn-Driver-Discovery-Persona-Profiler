package com.telechurn.ai.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.telechurn.ai.repository.CustomerRepository;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void summaryCalculatesRatesFromRepositoryCounts() {
        when(customerRepository.count()).thenReturn(100L);
        when(customerRepository.countByChurn("Yes")).thenReturn(25L);
        when(customerRepository.countByRiskLevel("HIGH")).thenReturn(10L);

        DashboardService service = new DashboardService(customerRepository);
        Map<String, Object> summary = service.summary();

        assertThat(summary.get("totalCustomers")).isEqualTo(100L);
        assertThat(summary.get("churnedCustomers")).isEqualTo(25L);
        assertThat(summary.get("highRiskCustomers")).isEqualTo(10L);
        assertThat((Double) summary.get("churnRate")).isEqualTo(25.0);
    }
}
