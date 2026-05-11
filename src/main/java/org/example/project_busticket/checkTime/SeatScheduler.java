package org.example.project_busticket.checkTime;

import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Enums.SeatStatus;
import org.example.project_busticket.model.Seat;
import org.example.project_busticket.repository.SeatRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatScheduler {

    private final SeatRepository seatRepository;

    @Scheduled(fixedRate = 60000) // chạy mỗi 60 giây
    public void clearExpiredSeats() {

        List<Seat> seats = seatRepository.findAll();

        for (Seat seat : seats) {

            if (seat.getStatus() == SeatStatus.PENDING
                    && seat.getTimeout() != null
                    && seat.getTimeout().isBefore(LocalDateTime.now())) {

                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setTimeout(null);

                seatRepository.save(seat);
            }
        }
    }
}