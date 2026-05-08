package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.service.BookService;
import com.service.IssueService;
import com.service.UserService;
import com.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final BookService  bookService;
    private final UserService  userService;

    // Process issue
    @PostMapping
    public String issueBook(@RequestParam Long bookId,
                            @RequestParam Long userId,
                            RedirectAttributes ra) {
        issueService.issueBook(bookId, userId);
        ra.addFlashAttribute("successMessage", "Book issued successfully!");
        return "redirect:/issues";
    }

    @GetMapping
    public String listIssues(
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        var issuePage = issueService.getAllIssues(status, page, size);
        model.addAttribute("issuePage", issuePage);
        model.addAttribute("pageNumbers", CommonUtil.getPageNumbers(issuePage));
        model.addAttribute("currentPage", page);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("overdueCount",
            issueService.getOverdueIssues().size());
        return "issues/list";
    }

    // Overdue list
    @GetMapping("/overdue")
    public String overdueList(Model model) {
        model.addAttribute("overdueIssues", issueService.getOverdueIssues());
        return "issues/overdue";
    }

    // Return book
    @GetMapping("/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes ra) {
        var dto = issueService.returnBook(id);
        String msg = "Book returned!";
        if (dto.getFineAmount() > 0)
            msg += " Fine collected: ₹" + dto.getFineAmount();
        ra.addFlashAttribute("successMessage", msg);
        return "redirect:/issues";
    }

    // Show issue-book form
    @GetMapping("/new")
    public String showIssueForm(Model model) {
        model.addAttribute("books",
            bookService.getAllBooks("", 0, 100, "bookTitle", "asc").getContent());
        model.addAttribute("users",
            userService.getAllUsers("", 0, 100, "userName", "asc").getContent());
        return "issues/form";
    }
}