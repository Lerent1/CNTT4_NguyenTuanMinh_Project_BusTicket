package org.example.project_busticket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Enums.Role;
import org.example.project_busticket.model.User;
import org.example.project_busticket.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

    private final AuthService authService;

    @GetMapping("/addEmployee")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "admin/addEmployee";
    }

    @PostMapping("/addEmployee")
    public String save(@Valid @ModelAttribute("user") User user,
                       BindingResult result,
                       Model model) {

        if (result.hasErrors())
            return "admin/addEmployee";

        try {
            authService.createStaff(user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin/addEmployee";
        }

        return "redirect:/admin";
    }
}