package com.controller;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dto.BookDTO;
import com.service.BookService;
import com.util.CommonUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // DELETE (soft)
    @GetMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id, RedirectAttributes ra) {
        bookService.deleteBook(id);
        ra.addFlashAttribute("successMessage", "Book deleted successfully!");
        return "redirect:/books";
    }

    // LIST with search + paging + sorting
    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "bookTitle") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Page<BookDTO> bookPage = bookService.getAllBooks(keyword, page, size, sortBy, sortDir);

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("pageNumbers", CommonUtil.getPageNumbers(bookPage));
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("currentPage", page);
        model.addAttribute("genres", bookService.getAllGenres());
        return "books/list";
    }

    // SAVE NEW BOOK
    @PostMapping
    public String saveBook(@Valid @ModelAttribute("book") BookDTO bookDTO,
                           BindingResult result,
                           Model model,
                           RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Add New Book");
            return "books/form";
        }
        bookService.saveBook(bookDTO);
        ra.addFlashAttribute("successMessage", "Book added successfully!");
        return "redirect:/books";
    }

    // SHOW ADD FORM
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("book", new BookDTO());
        model.addAttribute("pageTitle", "Add New Book");
        return "books/form";
    }

    // SHOW EDIT FORM
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        model.addAttribute("pageTitle", "Edit Book");
        return "books/form";
    }

    // UPDATE BOOK
    @PostMapping("/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") BookDTO bookDTO,
                             BindingResult result,
                             Model model,
                             RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Book");
            return "books/form";
        }
        bookService.updateBook(id, bookDTO);
        ra.addFlashAttribute("successMessage", "Book updated successfully!");
        return "redirect:/books";
    }

    // VIEW DETAIL
    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "books/detail";
    }
}