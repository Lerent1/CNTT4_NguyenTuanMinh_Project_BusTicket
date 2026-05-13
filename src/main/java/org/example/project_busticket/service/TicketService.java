package org.example.project_busticket.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.enums.SeatStatus;
import org.example.project_busticket.model.enums.TicketStatus;
import org.example.project_busticket.model.Seat;
import org.example.project_busticket.model.Ticket;
import org.example.project_busticket.repository.SeatRepository;
import org.example.project_busticket.repository.TicketRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    // ================= SEARCH =================
    public Ticket findTicketDetail(String code, String phone) {
        return ticketRepository.findTicketDetail(code, phone)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé"));
    }

    public List<Ticket> getPendingTickets() {
        return ticketRepository.findByStatus(TicketStatus.PENDING);
    }

    public List<Ticket> findByUserId(Long userId) {
        return ticketRepository.findByUser_Id(userId);
    }

    @Transactional
    public void confirmTicket(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé"));

        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new RuntimeException("Chỉ xác nhận vé đang chờ thanh toán");
        }

        ticket.setStatus(TicketStatus.PAID);
        ticketRepository.save(ticket);
    }

    @Transactional
    public void cancelTicket(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé"));

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new RuntimeException("Vé đã bị hủy");
        }

        if (ticket.getStatus() == TicketStatus.PAID) {
            throw new RuntimeException("Không thể hủy vé đã thanh toán");
        }

        if (ticket.getTrip() == null || ticket.getTrip().getDepartureTime() == null) {
            throw new RuntimeException("Thiếu thông tin chuyến đi");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departureTime = ticket.getTrip().getDepartureTime();

        if (departureTime.isBefore(now)) {
            throw new RuntimeException("Vé đã quá giờ khởi hành");
        }

        if (departureTime.isBefore(now.plusHours(12))) {
            throw new RuntimeException("Chỉ được hủy trước 12 tiếng");
        }

        ticket.setStatus(TicketStatus.CANCELLED);

        Seat seat = ticket.getSeat();
        if (seat != null) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setTimeout(null);
            seatRepository.save(seat);
        }

        ticketRepository.save(ticket);
    }

    // ================= HƯỚNG 4 =================
    public List<Object[]> getRevenueByMonth() {
        return ticketRepository.getRevenueByRoute();
    }

    public List<Object[]> getTop5RevenueByBus() {
        Pageable top5 = PageRequest.of(0, 5);
        return ticketRepository.getTop5RevenueByBus(top5);
    }
}