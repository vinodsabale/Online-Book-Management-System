package com.controller;
 
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.entity.User;
import com.service.BookService;
import com.service.IssueService;
import com.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final BookService  bookService;
    private final UserService  userService;
    private final IssueService issueService;

 
    @GetMapping("/users/add")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "users/form";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userName", userDetails.getUsername());
        model.addAttribute("totalBooks", bookService.getTotalBooksCount());
        model.addAttribute("totalUsers", userService.getTotalUsersCount());
        model.addAttribute("activeIssues", issueService.getActiveIssuesCount());
        model.addAttribute("overdueIssues", issueService.getOverdueIssues().size());
        model.addAttribute("lowStockBooks", bookService.getLowStockBooks());
        model.addAttribute("recentIssues",
                issueService.getAllIssues("", 0, 5).getContent());
        return "librarian/dashboard";
    }
}