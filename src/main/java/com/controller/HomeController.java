package com.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
   
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isLibrarian = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN"));

        if (isAdmin) return "redirect:/dashboard";
        if (isLibrarian) return "redirect:/librarian/dashboard";
        return "redirect:/member/dashboard";
    }
}