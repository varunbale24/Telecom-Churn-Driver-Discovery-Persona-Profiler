package com.telechurn.ai.service;

import com.telechurn.ai.ml.DecisionTreeService;
import com.telechurn.ai.ml.KMeansService;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ModelService {

    private final DecisionTreeService decisionTreeService;
    private final KMeansService kMeansService;

    public ModelService(DecisionTreeService decisionTreeService, KMeansService kMeansService) {
        this.decisionTreeService = decisionTreeService;
        this.kMeansService = kMeansService;
    }

    public Map<String, Object> snapshot() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("decisionTree", decisionTreeService.snapshot());
        data.put("kmeans", kMeansService.snapshot());
        return data;
    }
}
