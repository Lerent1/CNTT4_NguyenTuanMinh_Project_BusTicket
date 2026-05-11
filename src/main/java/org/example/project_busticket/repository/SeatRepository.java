package org.example.project_busticket.repository;

import jakarta.persistence.LockModeType;
import org.example.project_busticket.model.Seat;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByTripId(Integer tripId);

    // lock ghế để chống đặt trùng
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :seatId")
    Optional<Seat> findSeatForUpdate(
            @Param("seatId") Long seatId
    );
}