package com.telechurn.ai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String customerId;

    @Column(nullable = false)
    private Integer tenure;

    @Column(nullable = false)
    private String contract;

    @Column(nullable = false)
    private String internetService;

    @Column(nullable = false)
    private Double monthlyCharges;

    @Column(nullable = false)
    private Double totalCharges;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private boolean seniorCitizen;

    @Column(nullable = false)
    private boolean partner;

    @Column(nullable = false)
    private boolean dependents;

    @Column(nullable = false)
    private String phoneService;

    @Column(nullable = false)
    private String multipleLines;

    @Column(nullable = false)
    private String onlineSecurity;

    @Column(nullable = false)
    private String onlineBackup;

    @Column(nullable = false)
    private String deviceProtection;

    @Column(nullable = false)
    private String techSupport;

    @Column(nullable = false)
    private String streamingTV;

    @Column(nullable = false)
    private String streamingMovies;

    @Column(nullable = false)
    private String paperlessBilling;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String churn;

    @Column(nullable = false)
    private String riskLevel = "LOW";

    @Column(nullable = false)
    private String personaName = "Unassigned";

    @Column(nullable = false)
    private Double churnProbability = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id")
    private Persona persona;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getTenure() {
        return tenure;
    }

    public void setTenure(Integer tenure) {
        this.tenure = tenure;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getInternetService() {
        return internetService;
    }

    public void setInternetService(String internetService) {
        this.internetService = internetService;
    }

    public Double getMonthlyCharges() {
        return monthlyCharges;
    }

    public void setMonthlyCharges(Double monthlyCharges) {
        this.monthlyCharges = monthlyCharges;
    }

    public Double getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(Double totalCharges) {
        this.totalCharges = totalCharges;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSeniorCitizen() {
        return seniorCitizen;
    }

    public void setSeniorCitizen(boolean seniorCitizen) {
        this.seniorCitizen = seniorCitizen;
    }

    public boolean isPartner() {
        return partner;
    }

    public void setPartner(boolean partner) {
        this.partner = partner;
    }

    public boolean isDependents() {
        return dependents;
    }

    public void setDependents(boolean dependents) {
        this.dependents = dependents;
    }

    public String getPhoneService() {
        return phoneService;
    }

    public void setPhoneService(String phoneService) {
        this.phoneService = phoneService;
    }

    public String getMultipleLines() {
        return multipleLines;
    }

    public void setMultipleLines(String multipleLines) {
        this.multipleLines = multipleLines;
    }

    public String getOnlineSecurity() {
        return onlineSecurity;
    }

    public void setOnlineSecurity(String onlineSecurity) {
        this.onlineSecurity = onlineSecurity;
    }

    public String getOnlineBackup() {
        return onlineBackup;
    }

    public void setOnlineBackup(String onlineBackup) {
        this.onlineBackup = onlineBackup;
    }

    public String getDeviceProtection() {
        return deviceProtection;
    }

    public void setDeviceProtection(String deviceProtection) {
        this.deviceProtection = deviceProtection;
    }

    public String getTechSupport() {
        return techSupport;
    }

    public void setTechSupport(String techSupport) {
        this.techSupport = techSupport;
    }

    public String getStreamingTV() {
        return streamingTV;
    }

    public void setStreamingTV(String streamingTV) {
        this.streamingTV = streamingTV;
    }

    public String getStreamingMovies() {
        return streamingMovies;
    }

    public void setStreamingMovies(String streamingMovies) {
        this.streamingMovies = streamingMovies;
    }

    public String getPaperlessBilling() {
        return paperlessBilling;
    }

    public void setPaperlessBilling(String paperlessBilling) {
        this.paperlessBilling = paperlessBilling;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getChurn() {
        return churn;
    }

    public void setChurn(String churn) {
        this.churn = churn;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }

    public Double getChurnProbability() {
        return churnProbability;
    }

    public void setChurnProbability(Double churnProbability) {
        this.churnProbability = churnProbability;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
