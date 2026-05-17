package org.example.project_busticket.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Ticket;
import org.example.project_busticket.model.User;
import org.example.project_busticket.service.BookingService;
import org.example.project_busticket.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TicketController {

    private final BookingService bookingService;
    private final TicketService ticketService;

    // ================= BOOK =================
    @PostMapping("/book-ticket")
    public String bookTicket(@RequestParam Long seatId,
                             @RequestParam String customerName,
                             @RequestParam String phone,
                             @RequestParam String email,
                             HttpSession session) {

        Long userId = (Long) session.getAttribute("currentUserId");
        User user = (User) session.getAttribute("currentUser");

        if (userId == null || user == null) {
            return "redirect:/auth/login";
        }

        bookingService.bookTicket(seatId, customerName, phone, email, user);

        return "redirect:/myTicket";
    }

    // ================= MY TICKET =================
    @GetMapping("/myTicket")
    public String myTickets(HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("currentUserId");

        if (userId == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("tickets",
                ticketService.findByUserId(userId));

        return "myTicket";
    }

    // ================= SEARCH  =================
    @GetMapping("/ticket/search")
    public String ticketSearchPage() {
        return "ticketSearch";
    }

    // ================= SEARCH ACTION =================
    @GetMapping("/ticket/detail")
    public String ticketDetail(
            @RequestParam String code,
            @RequestParam String phone,
            Model model
    ) {

        Ticket ticket = ticketService.findTicketDetail(code, phone);

        if (ticket == null) {
            model.addAttribute("error", "Không tìm thấy vé");
            return "ticketSearch";
        }

        model.addAttribute("ticket", ticket);

        return "ticketDetail";
    }

    // ================= DETAIL =================
    @GetMapping("/ticket/detail/{id}")
    public String ticketDetail(@PathVariable Long id,
                               Model model) {

        Ticket ticket = ticketService.findById(id);

        if (ticket == null) {
            model.addAttribute("error", "Không tìm thấy vé");
            return "redirect:/myTicket";
        }

        model.addAttribute("ticket", ticket);

        return "ticketDetail";
    }

    // ================= CANCEL =================
    @PostMapping("/cancel/{id}")
    public String cancelTicket(@PathVariable Long id,
                               RedirectAttributes ra) {

        try {
            ticketService.cancelTicket(id);
            ra.addFlashAttribute("success", "Hủy vé thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/myTicket";
    }
}