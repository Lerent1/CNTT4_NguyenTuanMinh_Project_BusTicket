package org.example.project_busticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Enums.Role;
import org.example.project_busticket.model.User;
import org.example.project_busticket.model.UserProfiles;
import org.example.project_busticket.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(Model model) {

        model.addAttribute("user", new User());

        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {

        User user = new User();
        user.setProfile(new UserProfiles());

        model.addAttribute("user", user);

        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) return "register";

        try {
            authService.registerPassenger(user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        return "redirect:/auth/login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @RequestParam String username,
                        @RequestParam String password,
                        Model model) {

        User user = authService.login(username, password);

        if (user == null) {
            model.addAttribute("error", "Sai tai khoan hoac mat khau");
            return "login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("currentUser", user.getUsername());
        session.setAttribute("currentRole", user.getRole().name());

        if (user.getRole() == Role.ADMIN)
            return "redirect:/admin";

        if (user.getRole() == Role.STAFF)
            return "redirect:/admin/tickets";

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}