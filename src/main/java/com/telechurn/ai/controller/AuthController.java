package com.telechurn.ai.controller;

import com.telechurn.ai.dto.RegisterRequest;
import com.telechurn.ai.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (request.getRole() == null) {
            request.setRole(com.telechurn.ai.entity.User.Role.VIEWER);
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.registerRequest", "Passwords do not match");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            authService.register(request);
            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully. Please sign in.");
            return "redirect:/login";
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("email", "error.registerRequest", exception.getMessage());
            return "register";
        }
    }
}
