package com.telechurn.ai.config;

import com.telechurn.ai.entity.Persona;
import com.telechurn.ai.entity.User;
import com.telechurn.ai.ml.DecisionTreeService;
import com.telechurn.ai.ml.KMeansService;
import com.telechurn.ai.repository.CustomerRepository;
import com.telechurn.ai.repository.PersonaRepository;
import com.telechurn.ai.repository.UserRepository;
import com.telechurn.ai.service.DatasetService;
import com.telechurn.ai.service.PersonaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PersonaRepository personaRepository;
    private final DatasetService datasetService;
    private final PersonaService personaService;
    private final DecisionTreeService decisionTreeService;
    private final KMeansService kMeansService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CustomerRepository customerRepository, PersonaRepository personaRepository, DatasetService datasetService, PersonaService personaService, DecisionTreeService decisionTreeService, KMeansService kMeansService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.personaRepository = personaRepository;
        this.datasetService = datasetService;
        this.personaService = personaService;
        this.decisionTreeService = decisionTreeService;
        this.kMeansService = kMeansService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("admin@telechurn.com")) {
            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setEmail("admin@telechurn.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
        }
        if (personaRepository.count() == 0) {
            createDefaultPersonas();
        }
        datasetService.loadIfEmpty();
        if (customerRepository.count() > 0) {
            personaService.assignPersonas();
            decisionTreeService.trainIfNeeded();
            kMeansService.trainIfNeeded();
        }
    }

    private void createDefaultPersonas() {
        Persona loyal = new Persona();
        loyal.setName("Loyal Customers");
        loyal.setRiskLevel("LOW");
        loyal.setDescription("Long-tenure customers with stable subscription behavior and low churn tendency.");
        personaRepository.save(loyal);

        Persona newRisk = new Persona();
        newRisk.setName("New / At-Risk Customers");
        newRisk.setRiskLevel("HIGH");
        newRisk.setDescription("Short-tenure customers with elevated churn exposure.");
        personaRepository.save(newRisk);

        Persona premium = new Persona();
        premium.setName("Premium Customers");
        premium.setRiskLevel("MEDIUM");
        premium.setDescription("High-value customers with richer service adoption.");
        personaRepository.save(premium);

        Persona priceSensitive = new Persona();
        priceSensitive.setName("Price-Sensitive Customers");
        priceSensitive.setRiskLevel("MEDIUM");
        priceSensitive.setDescription("Customers sensitive to charges and contract commitments.");
        personaRepository.save(priceSensitive);
    }
}
