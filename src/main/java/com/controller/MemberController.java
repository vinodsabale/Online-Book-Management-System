package com.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dto.BookDTO;
import com.dto.IssueDTO;
import com.entity.User;
import com.repository.UserRepository;
import com.service.BookService;
import com.service.IssueService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final IssueService issueService;
    private final BookService bookService;
    private final UserRepository userRepository;

    @GetMapping("/dashboard")
    public String memberDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow();

        List<IssueDTO> myIssues = issueService.getIssuesByUser(user.getUserId());
        double totalFine = myIssues.stream()
                .mapToDouble(IssueDTO::getFineAmount)
                .sum();
        List<BookDTO> newBooks = bookService.getAllBooks("", 0, 10, "bookId", "desc").getContent();

        model.addAttribute("userName", user.getUserName());
        model.addAttribute("myIssues", myIssues);
        model.addAttribute("totalFine", totalFine);
        model.addAttribute("newBooks", newBooks);
        return "member/dashboard";
    }

    @GetMapping("/my-books")
    public String myBooks(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("myIssues", issueService.getIssuesByUser(user.getUserId()));
        return "member/my-books";
    }

    @GetMapping("/new-books")
    public String newBooks(Model model) {
        model.addAttribute("newBooks",
                bookService.getAllBooks("", 0, 20, "bookId", "desc").getContent());
        return "member/new-books";
    }
}