package com.lms.controllers;

import com.lms.models.User;
import com.lms.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // enable API calls from frontend later
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        User saved = authService.registerUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("id", saved.getId());
        response.put("role", saved.getRole());
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Optional<User> userOpt = authService.login(username, password);
        Map<String, Object> response = new HashMap<>();

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            response.put("status", "ok");
            response.put("role", user.getRole());
        } else {
            response.put("status", "error");
            response.put("message", "Invalid username or password");
        }

        return response;
    }
}
