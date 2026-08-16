package com.telechurn.ai.controller;

import com.telechurn.ai.dto.PredictionRequest;
import com.telechurn.ai.dto.PredictionResponse;
import com.telechurn.ai.service.PredictionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping("/prediction")
    public String predictionForm(Model model) {
        model.addAttribute("predictionRequest", new PredictionRequest());
        return "prediction";
    }

    @PostMapping("/prediction")
    public String predict(@Valid @ModelAttribute("predictionRequest") PredictionRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "prediction";
        }
        PredictionResponse response = predictionService.predict(request);
        model.addAttribute("result", response);
        return "prediction";
    }
}
