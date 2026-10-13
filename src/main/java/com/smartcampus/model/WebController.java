package com.smartcampus.controller;

import com.smartcampus.model.User;
import com.smartcampus.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public WebController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {
        String e = email.trim().toLowerCase();
        model.addAttribute("email", e);

        if (!e.endsWith("@campus.com") || e.equals("admin@campus.com")) {
            model.addAttribute("error", "Please use your student email ending with @campus.com");
            return "register";
        }
        if (password.length() < 4) {
            model.addAttribute("error", "Password must be at least 4 characters");
            return "register";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }
        if (userRepository.findByEmail(e).isPresent()) {
            model.addAttribute("error", "This email is already registered. Please sign in instead.");
            return "register";
        }

        User u = new User();
        u.setEmail(e);
        u.setPassword(passwordEncoder.encode(password));
        u.setRole("STUDENT");
        userRepository.save(u);
        return "redirect:/login?registered";
    }

    @GetMapping("/student/home")
    public String studentHome(Authentication auth, Model model) {
        fill(auth, model, "STUDENT");
        return "dashboard";
    }

    @GetMapping("/admin/home")
    public String adminHome(Authentication auth, Model model) {
        fill(auth, model, "ADMIN");
        model.addAttribute("userCount", userRepository.count());
        return "dashboard";
    }

    private void fill(Authentication auth, Model model, String role) {
        String email = auth.getName();
        String name = email.substring(0, email.indexOf('@'));
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        model.addAttribute("role", role);
    }
}