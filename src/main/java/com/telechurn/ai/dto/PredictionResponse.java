package com.telechurn.ai.dto;

import java.util.List;

public class PredictionResponse {

    private double probability;
    private String riskLevel;
    private List<String> reasons;
    private List<String> recommendations;

    public PredictionResponse() {
    }

    public PredictionResponse(double probability, String riskLevel, List<String> reasons, List<String> recommendations) {
        this.probability = probability;
        this.riskLevel = riskLevel;
        this.reasons = reasons;
        this.recommendations = recommendations;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }
}
