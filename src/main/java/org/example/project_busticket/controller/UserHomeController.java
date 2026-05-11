package org.example.project_busticket.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserHomeController {

    @GetMapping("/home")
    public String home(HttpSession session) {

        String role = (String) session.getAttribute("currentRole");

        if (role == null)
            return "redirect:/auth/login";

        if ("STAFF".equals(role))
            return "redirect:/admin/tickets";

        return "home";
    }
}