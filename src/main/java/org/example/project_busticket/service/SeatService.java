package org.example.project_busticket.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.enums.SeatStatus;
import org.example.project_busticket.model.Seat;
import org.example.project_busticket.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    public List<Seat> getSeatsByTrip(Integer tripId) {
        return seatRepository.findByTripId(tripId);
    }

    @Transactional
    public void holdSeat(Long seatId) {

        Seat seat = seatRepository.findSeatForUpdate(seatId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ghế"));

        // đã book
        if (seat.getStatus() == SeatStatus.BOOKED) {
            throw new RuntimeException("Ghế đã được đặt");
        }

        // đang giữ và còn hạn
        if (seat.getStatus() == SeatStatus.PENDING
                && seat.getTimeout() != null
                && seat.getTimeout().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Ghế đang được giữ");
        }

        // set giữ ghế
        seat.setStatus(SeatStatus.PENDING);
        seat.setTimeout(LocalDateTime.now().plusMinutes(15));

        seatRepository.save(seat);
    }
}