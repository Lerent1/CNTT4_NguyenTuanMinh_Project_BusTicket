package org.example.project_busticket.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/tickets")
public class EmployeeController {

    private final TicketService ticketService;

    @GetMapping
    public String listPending(HttpSession session, Model model) {

        String role = (String) session.getAttribute("currentRole");

        if (!"STAFF".equals(role)) {
            return "redirect:/home";
        }

        model.addAttribute("tickets", ticketService.getPendingTickets());

        return "employee/ticketList";
    }

    @PostMapping("/confirm/{id}")
    public String confirm(@PathVariable Long id, HttpSession session) {

        String role = (String) session.getAttribute("currentRole");
        if (!"STAFF".equals(role)) {
            return "redirect:/home";
        }

        ticketService.confirmTicket(id);

        return "redirect:/admin/tickets";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id, HttpSession session) {

        String role = (String) session.getAttribute("currentRole");
        if (!"STAFF".equals(role)) {
            return "redirect:/home";
        }

        ticketService.cancelTicket(id);

        return "redirect:/admin/tickets";
    }
}