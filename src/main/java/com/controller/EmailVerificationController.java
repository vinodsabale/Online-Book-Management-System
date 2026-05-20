package com.controller;

import com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class EmailVerificationController {

    private final UserService userService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, Model model) {
        boolean success = userService.verifyEmail(token);
        if (success) {
            model.addAttribute("message", "Your email has been verified! You can now login.");
            model.addAttribute("success", true);
        } else {
            model.addAttribute("message", "Invalid or expired verification link.");
            model.addAttribute("success", false);
        }
        return "verify-email";
    }
}