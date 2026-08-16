package com.telechurn.ai.service;

import com.telechurn.ai.dto.PredictionRequest;
import com.telechurn.ai.dto.PredictionResponse;
import com.telechurn.ai.ml.DataPreprocessingService;
import com.telechurn.ai.ml.DecisionTreeService;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    private final DecisionTreeService decisionTreeService;

    public PredictionService(DecisionTreeService decisionTreeService) {
        this.decisionTreeService = decisionTreeService;
    }

    public PredictionResponse predict(PredictionRequest request) {
        var features = new DataPreprocessingService.PredictionFeatures(
                request.getTenure(),
                request.getMonthlyCharges(),
                request.getTotalCharges(),
                request.getContract(),
                request.getInternetService(),
                request.getTechSupport(),
                request.getOnlineSecurity()
        );
        var result = decisionTreeService.predict(features);
        return new PredictionResponse(result.probability(), result.riskLevel(), result.reasons(), result.recommendations());
    }
}
