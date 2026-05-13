package org.example.project_busticket.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.enums.SeatStatus;
import org.example.project_busticket.model.enums.TicketStatus;
import org.example.project_busticket.model.Seat;
import org.example.project_busticket.model.Ticket;
import org.example.project_busticket.repository.SeatRepository;
import org.example.project_busticket.repository.TicketRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketSchedulerService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    @Scheduled(fixedRate = 10 * 60 * 1000) // 10 phút
    @Transactional
    public void autoCancelExpiredTickets() {

        LocalDateTime limit = LocalDateTime.now().minusMinutes(30);

        List<Ticket> expired = ticketRepository.findExpiredTickets(TicketStatus.PENDING, limit);

        for (Ticket ticket : expired) {

            ticket.setStatus(TicketStatus.CANCELLED);

            Seat seat = ticket.getSeat();
            if (seat != null) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setTimeout(null);
                seatRepository.save(seat);
            }

            ticketRepository.save(ticket);
        }
    }
}