package org.example.project_busticket.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Ticket;
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

    @PostMapping("/book-ticket")
    public String bookTicket(
            @RequestParam Long seatId,
            @RequestParam String customerName,
            @RequestParam String phone,
            @RequestParam String email,
            HttpSession session
    ) {

        session.setAttribute("phone", phone);

        Ticket ticket = bookingService.bookTicket(seatId, customerName, phone, email);

        return "redirect:/ticket/search?code=" + ticket.getCode() + "&phone=" + ticket.getPhone();
    }

    @GetMapping("/ticket/search")
    public String searchTicket(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String phone,
            Model model
    ) {

        if (code == null || phone == null) {
            return "ticketSearch";
        }

        try {
            Ticket ticket = ticketService.findTicketDetail(code, phone);
            model.addAttribute("ticket", ticket);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "ticketDetail";
    }

    @GetMapping("/myTicket")
    public String myTickets(HttpSession session, Model model) {

        String phone = (String) session.getAttribute("phone");

        if (phone == null) {
            model.addAttribute("tickets", List.of());
            model.addAttribute("error", "Chưa có lịch sử vé");
            return "myTicket";
        }

        model.addAttribute("tickets", ticketService.findByPhone(phone));
        return "myTicket";
    }

    @PostMapping("/cancel/{id}")
    public String cancelTicket(@PathVariable Long id, RedirectAttributes ra) {

        try {
            ticketService.cancelTicket(id);
            ra.addFlashAttribute("success", "Hủy vé thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/myTicket";
    }
}