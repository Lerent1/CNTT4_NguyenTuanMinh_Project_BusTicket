package org.example.project_busticket.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Enums.SeatStatus;
import org.example.project_busticket.model.Enums.TicketStatus;
import org.example.project_busticket.model.Seat;
import org.example.project_busticket.model.Ticket;
import org.example.project_busticket.repository.SeatRepository;
import org.example.project_busticket.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    @Transactional
    public Ticket bookTicket(Long seatId, String customerName, String phone, String email) {

        Seat seat = seatRepository.findSeatForUpdate(seatId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ghế"));

        // ❌ nếu đã BOOKED
        if (seat.getStatus() == SeatStatus.BOOKED) {
            throw new RuntimeException("Ghế đã được đặt");
        }

        // ❌ nếu không phải PENDING thì không được book
        if (seat.getStatus() != SeatStatus.PENDING) {
            throw new RuntimeException("Ghế chưa được giữ");
        }

        // ❌ timeout expired check phải đứng trước logic booking
        if (seat.getTimeout() == null ||
                seat.getTimeout().isBefore(LocalDateTime.now())) {

            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setTimeout(null);
            seatRepository.save(seat);

            throw new RuntimeException("Phiên giữ ghế đã hết hạn");
        }

        // ===================== CREATE TICKET =====================
//        Ticket ticket = new Ticket();
//        ticket.setCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
//        ticket.setCustomerName(customerName);
//        ticket.setPhone(phone);
//        ticket.setSeat(seat);
//        ticket.setTrip(seat.getTrip());
//        ticket.setTotalPrice(seat.getTrip().getPrice());
//        ticket.setStatus(TicketStatus.PENDING);
//        ticket.setCreatedAt(LocalDateTime.now());

        // ===================== CREATE TICKET =====================
        Ticket ticket = new Ticket();
        ticket.setCode(UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase());

        ticket.setCustomerName(customerName);
        ticket.setPhone(phone);
        ticket.setEmail(email);   // 🔥 BẮT BUỘC PHẢI CÓ

        ticket.setSeat(seat);
        ticket.setTrip(seat.getTrip());
        ticket.setTotalPrice(seat.getTrip().getPrice());
        ticket.setStatus(TicketStatus.PENDING);

        // ===================== UPDATE SEAT =====================
//        seat.setStatus(SeatStatus.BOOKED);
//        seat.setTimeout(null);
//        seatRepository.save(seat);
//
//        return ticketRepository.save(ticket);

        // ===================== UPDATE SEAT =====================
        seat.setStatus(SeatStatus.BOOKED);
        seat.setTimeout(null);
        seatRepository.save(seat);

        Ticket savedTicket = ticketRepository.save(ticket);

        // ===================== SEND EMAIL ASYNC =====================
        emailService.sendTicketConfirmation(
                savedTicket.getEmail(),
                savedTicket.getCode()
        );

        return savedTicket;
    }
}