package com.telechurn.ai.controller;

import com.telechurn.ai.service.ModelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping("/model-performance")
    public String performance(Model model) {
        model.addAttribute("snapshot", modelService.snapshot());
        return "model-performance";
    }
}
