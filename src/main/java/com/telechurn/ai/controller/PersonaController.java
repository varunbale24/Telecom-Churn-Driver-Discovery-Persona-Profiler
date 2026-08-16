package com.telechurn.ai.controller;

import com.telechurn.ai.service.PersonaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping("/personas")
    public String personas(Model model) {
        model.addAttribute("personaMetrics", personaService.personaMetrics());
        model.addAttribute("personas", personaService.allPersonas());
        return "personas";
    }
}
