package com.telechurn.ai.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.telechurn.ai.dto.PredictionRequest;
import com.telechurn.ai.ml.DecisionTreeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PredictionServiceTest {

    @Mock
    private DecisionTreeService decisionTreeService;

    @Test
    void predictReturnsMappedResponse() {
        when(decisionTreeService.predict(org.mockito.ArgumentMatchers.any())).thenReturn(
                new DecisionTreeService.PredictionResult(0.82, "HIGH", java.util.List.of("Low tenure"), java.util.List.of("Offer long-term contract incentive")));

        PredictionService service = new PredictionService(decisionTreeService);
        PredictionRequest request = new PredictionRequest();
        request.setTenure(5);
        request.setMonthlyCharges(85.0);
        request.setTotalCharges(425.0);
        request.setContract("Month-to-month");
        request.setInternetService("Fiber optic");
        request.setTechSupport("No");
        request.setOnlineSecurity("No");

        var response = service.predict(request);

        assertThat(response.getProbability()).isEqualTo(0.82);
        assertThat(response.getRiskLevel()).isEqualTo("HIGH");
        assertThat(response.getReasons()).contains("Low tenure");
    }
}
