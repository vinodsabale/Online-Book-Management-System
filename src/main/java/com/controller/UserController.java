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

import com.dto.UserDTO;
import com.entity.User.UserRole;
import com.service.UserService;
import com.util.CommonUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        userService.deleteUser(id);
        ra.addFlashAttribute("successMessage", "User removed.");
        return "redirect:/users";
    }

    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "userName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Page<UserDTO> userPage = userService.getAllUsers(keyword, page, size, sortBy, sortDir);
        model.addAttribute("userPage", userPage);
        model.addAttribute("pageNumbers", CommonUtil.getPageNumbers(userPage));
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("currentPage", page);
        model.addAttribute("roles", UserRole.values());
        return "users/list";
    }

    @PostMapping
    public String saveUser(@Valid @ModelAttribute("user") UserDTO userDTO,
                           BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("pageTitle", "Add New User");
            return "users/form";
        }
        userService.saveUser(userDTO);
        ra.addFlashAttribute("successMessage", "User added successfully!");
        return "redirect:/users";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("pageTitle", "Add New User");
        return "users/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("pageTitle", "Edit User");
        return "users/form";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("user") UserDTO userDTO,
                             BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("roles", UserRole.values());
            return "users/form";
        }
        userService.updateUser(id, userDTO);
        ra.addFlashAttribute("successMessage", "User updated!");
        return "redirect:/users";
    }
}