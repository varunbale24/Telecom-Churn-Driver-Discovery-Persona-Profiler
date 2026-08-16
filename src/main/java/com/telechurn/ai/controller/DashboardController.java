package com.telechurn.ai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telechurn.ai.service.ChurnService;
import com.telechurn.ai.service.DashboardService;
import com.telechurn.ai.service.PersonaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final ChurnService churnService;
    private final PersonaService personaService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DashboardController(DashboardService dashboardService, ChurnService churnService, PersonaService personaService) {
        this.dashboardService = dashboardService;
        this.churnService = churnService;
        this.personaService = personaService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) throws JsonProcessingException {
        model.addAttribute("summary", dashboardService.summary());
        model.addAttribute("contractChart", objectMapper.writeValueAsString(churnService.churnByContract()));
        model.addAttribute("internetChart", objectMapper.writeValueAsString(churnService.churnByInternetService()));
        model.addAttribute("paymentChart", objectMapper.writeValueAsString(churnService.churnByPaymentMethod()));
        model.addAttribute("tenureChart", objectMapper.writeValueAsString(churnService.churnByTenureBand()));
        model.addAttribute("driverChart", objectMapper.writeValueAsString(churnService.topDrivers()));
        model.addAttribute("personaMetrics", personaService.personaMetrics());
        model.addAttribute("insights", churnService.insights());
        model.addAttribute("username", authentication != null ? authentication.getName() : "Analyst");
        return "dashboard";
    }
}
