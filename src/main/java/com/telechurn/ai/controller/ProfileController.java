package com.telechurn.ai.controller;

import com.telechurn.ai.dto.ProfileRequest;
import com.telechurn.ai.entity.User;
import com.telechurn.ai.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setFullName(user.getFullName());
        profileRequest.setEmail(user.getEmail());
        profileRequest.setRole(user.getRole());

        model.addAttribute("profileRequest", profileRequest);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("profileRequest") ProfileRequest profileRequest,
                                BindingResult bindingResult,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Validate password if provided
        if (profileRequest.getPassword() != null && !profileRequest.getPassword().isBlank()) {
            if (profileRequest.getPassword().length() < 8) {
                bindingResult.rejectValue("password", "error.profileRequest", "Password must be at least 8 characters");
            }
            if (!profileRequest.getPassword().equals(profileRequest.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "error.profileRequest", "Passwords do not match");
            }
        }

        if (bindingResult.hasErrors()) {
            return "profile";
        }

        // Check if email is already taken by another user
        if (!profileRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(profileRequest.getEmail()).isPresent()) {
                bindingResult.rejectValue("email", "error.profileRequest", "Email already exists");
                return "profile";
            }
        }

        // Update user details
        user.setFullName(profileRequest.getFullName());
        user.setEmail(profileRequest.getEmail());
        user.setRole(profileRequest.getRole());

        if (profileRequest.getPassword() != null && !profileRequest.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(profileRequest.getPassword()));
        }

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully! Changes saved.");
        return "redirect:/profile";
    }
}
