package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dto.BookDTO;
import com.service.BookService;
import com.service.IssueService;
import com.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/librarian")
@RequiredArgsConstructor
public class LibrarianController {

    private final BookService bookService;
    private final IssueService issueService;
    private final UserService userService;

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalBooks", bookService.getTotalBooksCount());
        model.addAttribute("totalUsers", userService.getTotalUsersCount());
        model.addAttribute("activeIssues", issueService.getActiveIssuesCount());
        model.addAttribute("overdueIssues", issueService.getOverdueIssues().size());
        model.addAttribute("lowStockBooks", bookService.getLowStockBooks());
        model.addAttribute("recentIssues",
                issueService.getAllIssues("", 0, 5).getContent());
        return "librarian/dashboard";
    }

    // View all books
    @GetMapping("/books")
    public String listBooks(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        var bookPage = bookService.getAllBooks(keyword, page, size, "bookTitle", "asc");
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        return "librarian/list";  
    }

    // Add book form
    @GetMapping("/books/new")
    public String showAddBook(Model model) {
        model.addAttribute("book", new BookDTO());
        model.addAttribute("pageTitle", "Add New Book");
        return "librarian/form";
    }

    // Save new book
    @PostMapping("/books")
    public String saveBook(@ModelAttribute("book") BookDTO bookDTO,
                           RedirectAttributes ra) {
        bookService.saveBook(bookDTO);
        ra.addFlashAttribute("successMessage", "Book added successfully!");
        return "redirect:/librarian/books";
    }

 
    @GetMapping("/books/{id}/edit")
    public String showEditBook(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        model.addAttribute("pageTitle", "Edit Book");
        return "librarian/form";
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id,
                             @ModelAttribute("book") BookDTO bookDTO,
                             RedirectAttributes ra) {
        bookService.updateBook(id, bookDTO);
        ra.addFlashAttribute("successMessage", "Book updated successfully!");
        return "redirect:/librarian/books";
    }

    // View all users (read only)
    @GetMapping("/users")
    public String listUsers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        model.addAttribute("userPage",
                userService.getAllUsers(keyword, page, 10, "userName", "asc"));
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        return "librarian/users";
    }

    // View all issues
    @GetMapping("/issues")
    public String listIssues(
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        var issuePage = issueService.getAllIssues(status, page, 10);
        model.addAttribute("issuePage", issuePage);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("overdueCount", issueService.getOverdueIssues().size());
        return "librarian/issues-list"; 
    }

    // Issue book form
    @GetMapping("/issues/new")
    public String showIssueForm(Model model) {
        model.addAttribute("books",
                bookService.getAllBooks("", 0, 100, "bookTitle", "asc").getContent());
        model.addAttribute("users",
                userService.getAllUsers("", 0, 100, "userName", "asc").getContent());
        return "librarian/issue-form";
    }

    // Process issue
    @PostMapping("/issues")
    public String issueBook(@RequestParam Long bookId,
                            @RequestParam Long userId,
                            RedirectAttributes ra) {
        issueService.issueBook(bookId, userId);
        ra.addFlashAttribute("successMessage", "Book issued successfully!");
        return "redirect:/librarian/issues";
    }

    // Return book
    @GetMapping("/issues/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes ra) {
        var dto = issueService.returnBook(id);
        String msg = "Book returned!";
        if (dto.getFineAmount() > 0)
            msg += " Fine collected: ₹" + dto.getFineAmount();
        ra.addFlashAttribute("successMessage", msg);
        return "redirect:/librarian/issues";
    }

    // Overdue list
    @GetMapping("/issues/overdue")
    public String overdueList(Model model) {
        model.addAttribute("overdueIssues", issueService.getOverdueIssues());
        return "librarian/overdue";
    }
}