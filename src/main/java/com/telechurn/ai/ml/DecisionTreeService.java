package com.telechurn.ai.ml;

import com.telechurn.ai.entity.Customer;
import com.telechurn.ai.repository.CustomerRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.List;
import org.springframework.stereotype.Service;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

@Service
public class DecisionTreeService {

    private final CustomerRepository customerRepository;
    private final DataPreprocessingService preprocessingService;
    private final ModelManager modelManager;
    private J48 model;
    private Evaluation evaluation;
    private String rules = "";

    public DecisionTreeService(CustomerRepository customerRepository, DataPreprocessingService preprocessingService, ModelManager modelManager) {
        this.customerRepository = customerRepository;
        this.preprocessingService = preprocessingService;
        this.modelManager = modelManager;
    }

    public synchronized void trainIfNeeded() {
        if (model != null) {
            return;
        }
        File modelFile = modelManager.decisionTreeFile();
        if (modelFile.exists()) {
            loadModel();
            return;
        }
        train();
    }

    public synchronized void train() {
        try {
            List<Customer> customers = customerRepository.findAll();
            if (customers.isEmpty()) {
                this.rules = "No training data available.";
                return;
            }
            Instances data = preprocessingService.buildTrainingData(customers);
            J48 tree = new J48();
            tree.buildClassifier(data);
            Evaluation eval = new Evaluation(data);
            eval.evaluateModel(tree, data);
            this.model = tree;
            this.evaluation = eval;
            this.rules = tree.toString();
            modelManager.ensureDirectory();
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(modelManager.decisionTreeFile()))) {
                outputStream.writeObject(tree);
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to train decision tree", exception);
        }
    }

    public synchronized void loadModel() {
        try (var inputStream = new java.io.ObjectInputStream(Files.newInputStream(modelManager.decisionTreeFile().toPath()))) {
            this.model = (J48) inputStream.readObject();
            this.rules = model.toString();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to load decision tree model", exception);
        }
    }

    public synchronized PredictionResult predict(DataPreprocessingService.PredictionFeatures features) {
        trainIfNeeded();
        try {
            Instances data = preprocessingService.buildPredictionInstance(features);
            Instance instance = data.instance(0);
            double distribution = model.distributionForInstance(instance)[1];
            String risk = distribution >= 0.7 ? "HIGH" : distribution >= 0.4 ? "MEDIUM" : "LOW";
            return new PredictionResult(distribution, risk, rulesFor(features), recommendationsFor(features, risk));
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to predict churn", exception);
        }
    }

    public ModelSnapshot snapshot() {
        trainIfNeeded();
        return new ModelSnapshot(
                evaluation != null ? evaluation.pctCorrect() : 0.0,
                evaluation != null ? evaluation.precision(1) : 0.0,
                evaluation != null ? evaluation.recall(1) : 0.0,
                evaluation != null ? evaluation.fMeasure(1) : 0.0,
                rules
        );
    }

    private List<String> rulesFor(DataPreprocessingService.PredictionFeatures features) {
        List<String> reasons = new java.util.ArrayList<>();
        if ("Month-to-month".equals(features.contract())) {
            reasons.add("Month-to-month contract");
        }
        if (features.tenure() != null && features.tenure() < 12) {
            reasons.add("Low tenure");
        }
        if (features.monthlyCharges() != null && features.monthlyCharges() > 70) {
            reasons.add("High monthly charges");
        }
        if ("No".equals(features.techSupport())) {
            reasons.add("No technical support");
        }
        if (reasons.isEmpty()) {
            reasons.add("Customer profile is relatively stable");
        }
        return reasons;
    }

    private List<String> recommendationsFor(DataPreprocessingService.PredictionFeatures features, String risk) {
        List<String> recommendations = new java.util.ArrayList<>();
        if ("HIGH".equals(risk)) {
            recommendations.add("Offer long-term contract incentive");
            recommendations.add("Provide technical support");
            recommendations.add("Offer personalized retention plan");
        } else if ("MEDIUM".equals(risk)) {
            recommendations.add("Review billing and pricing");
            recommendations.add("Promote security and backup services");
        } else {
            recommendations.add("Maintain service quality and loyalty benefits");
        }
        return recommendations;
    }

    public record PredictionResult(double probability, String riskLevel, List<String> reasons, List<String> recommendations) {}
    public record ModelSnapshot(double accuracy, double precision, double recall, double f1Score, String rules) {}
}
