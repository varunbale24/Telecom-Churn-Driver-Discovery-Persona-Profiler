package com.telechurn.ai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telechurn.ai.service.ChurnService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChurnController {

    private final ChurnService churnService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChurnController(ChurnService churnService) {
        this.churnService = churnService;
    }

    @GetMapping("/churn-analysis")
    public String analytics(Model model) throws JsonProcessingException {
        model.addAttribute("overallRate", churnService.overallChurnRate());
        model.addAttribute("contractChart", objectMapper.writeValueAsString(churnService.churnByContract()));
        model.addAttribute("internetChart", objectMapper.writeValueAsString(churnService.churnByInternetService()));
        model.addAttribute("paymentChart", objectMapper.writeValueAsString(churnService.churnByPaymentMethod()));
        model.addAttribute("insights", churnService.insights());
        return "churn-analysis";
    }

    @GetMapping("/churn-drivers")
    public String drivers(Model model) {
        model.addAttribute("rules", "IF Contract = Month-to-month AND Tenure < 12 months AND Monthly Charges > 70 THEN Risk = HIGH");
        return "churn-drivers";
    }
}
