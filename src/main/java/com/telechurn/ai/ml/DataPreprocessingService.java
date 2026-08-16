package com.telechurn.ai.ml;

import com.telechurn.ai.entity.Customer;
import java.util.List;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import org.springframework.stereotype.Service;

@Service
public class DataPreprocessingService {

    public Instances buildTrainingData(List<Customer> customers) {
        java.util.ArrayList<Attribute> attributes = new java.util.ArrayList<>();
        attributes.add(new Attribute("tenure"));
        attributes.add(new Attribute("monthlyCharges"));
        attributes.add(new Attribute("totalCharges"));
        attributes.add(new Attribute("contract", java.util.List.of("Month-to-month", "One year", "Two year")));
        attributes.add(new Attribute("internetService", java.util.List.of("DSL", "Fiber optic", "No")));
        attributes.add(new Attribute("techSupport", java.util.List.of("Yes", "No", "No internet service")));
        attributes.add(new Attribute("onlineSecurity", java.util.List.of("Yes", "No", "No internet service")));
        attributes.add(new Attribute("churn", java.util.List.of("No", "Yes")));
        Instances data = new Instances("telechurn", attributes, customers.size());
        data.setClassIndex(data.numAttributes() - 1);
        for (Customer customer : customers) {
            DenseInstance instance = new DenseInstance(data.numAttributes());
            instance.setDataset(data);
            instance.setValue(attributes.get(0), customer.getTenure());
            instance.setValue(attributes.get(1), customer.getMonthlyCharges());
            instance.setValue(attributes.get(2), customer.getTotalCharges());
            instance.setValue(attributes.get(3), customer.getContract());
            instance.setValue(attributes.get(4), customer.getInternetService());
            instance.setValue(attributes.get(5), customer.getTechSupport());
            instance.setValue(attributes.get(6), customer.getOnlineSecurity());
            instance.setValue(attributes.get(7), customer.getChurn());
            data.add(instance);
        }
        return data;
    }

    public Instances buildPredictionInstance(PredictionFeatures features) {
        java.util.ArrayList<Attribute> attributes = new java.util.ArrayList<>();
        attributes.add(new Attribute("tenure"));
        attributes.add(new Attribute("monthlyCharges"));
        attributes.add(new Attribute("totalCharges"));
        attributes.add(new Attribute("contract", java.util.List.of("Month-to-month", "One year", "Two year")));
        attributes.add(new Attribute("internetService", java.util.List.of("DSL", "Fiber optic", "No")));
        attributes.add(new Attribute("techSupport", java.util.List.of("Yes", "No", "No internet service")));
        attributes.add(new Attribute("onlineSecurity", java.util.List.of("Yes", "No", "No internet service")));
        attributes.add(new Attribute("churn", java.util.List.of("No", "Yes")));
        Instances data = new Instances("telechurn_prediction", attributes, 1);
        data.setClassIndex(data.numAttributes() - 1);
        DenseInstance instance = new DenseInstance(data.numAttributes());
        instance.setDataset(data);
        instance.setValue(attributes.get(0), features.tenure());
        instance.setValue(attributes.get(1), features.monthlyCharges());
        instance.setValue(attributes.get(2), features.totalCharges());
        instance.setValue(attributes.get(3), features.contract());
        instance.setValue(attributes.get(4), features.internetService());
        instance.setValue(attributes.get(5), features.techSupport());
        instance.setValue(attributes.get(6), features.onlineSecurity());
        data.add(instance);
        return data;
    }

    public record PredictionFeatures(Integer tenure, Double monthlyCharges, Double totalCharges, String contract, String internetService, String techSupport, String onlineSecurity) {}
}
