package com.telechurn.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PredictionRequest {

    // Essential fields for prediction
    @NotNull private Integer tenure;
    @NotBlank private String contract;
    @NotNull private Double monthlyCharges;
    @NotBlank private String internetService;
    @NotBlank private String techSupport;

    // Optional fields with defaults
    private String gender = "Male";
    private Boolean seniorCitizen = false;
    private Boolean partner = false;
    private Boolean dependents = false;
    private String phoneService = "No";
    private String multipleLines = "No";
    private String onlineSecurity = "No";
    private String onlineBackup = "No";
    private String deviceProtection = "No";
    private String streamingTV = "No";
    private String streamingMovies = "No";
    private String paperlessBilling = "No";
    private String paymentMethod = "Electronic check";
    private Double totalCharges = 0.0;

    // Getters and Setters
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Boolean getSeniorCitizen() { return seniorCitizen; }
    public void setSeniorCitizen(Boolean seniorCitizen) { this.seniorCitizen = seniorCitizen; }
    public Boolean getPartner() { return partner; }
    public void setPartner(Boolean partner) { this.partner = partner; }
    public Boolean getDependents() { return dependents; }
    public void setDependents(Boolean dependents) { this.dependents = dependents; }
    public Integer getTenure() { return tenure; }
    public void setTenure(Integer tenure) { this.tenure = tenure; }
    public String getPhoneService() { return phoneService; }
    public void setPhoneService(String phoneService) { this.phoneService = phoneService; }
    public String getMultipleLines() { return multipleLines; }
    public void setMultipleLines(String multipleLines) { this.multipleLines = multipleLines; }
    public String getInternetService() { return internetService; }
    public void setInternetService(String internetService) { this.internetService = internetService; }
    public String getOnlineSecurity() { return onlineSecurity; }
    public void setOnlineSecurity(String onlineSecurity) { this.onlineSecurity = onlineSecurity; }
    public String getOnlineBackup() { return onlineBackup; }
    public void setOnlineBackup(String onlineBackup) { this.onlineBackup = onlineBackup; }
    public String getDeviceProtection() { return deviceProtection; }
    public void setDeviceProtection(String deviceProtection) { this.deviceProtection = deviceProtection; }
    public String getTechSupport() { return techSupport; }
    public void setTechSupport(String techSupport) { this.techSupport = techSupport; }
    public String getStreamingTV() { return streamingTV; }
    public void setStreamingTV(String streamingTV) { this.streamingTV = streamingTV; }
    public String getStreamingMovies() { return streamingMovies; }
    public void setStreamingMovies(String streamingMovies) { this.streamingMovies = streamingMovies; }
    public String getContract() { return contract; }
    public void setContract(String contract) { this.contract = contract; }
    public String getPaperlessBilling() { return paperlessBilling; }
    public void setPaperlessBilling(String paperlessBilling) { this.paperlessBilling = paperlessBilling; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Double getMonthlyCharges() { return monthlyCharges; }
    public void setMonthlyCharges(Double monthlyCharges) { this.monthlyCharges = monthlyCharges; }
    public Double getTotalCharges() { return totalCharges; }
    public void setTotalCharges(Double totalCharges) { this.totalCharges = totalCharges; }
}
