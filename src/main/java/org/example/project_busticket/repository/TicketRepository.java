package org.example.project_busticket.repository;

import org.example.project_busticket.model.Enums.TicketStatus;
import org.example.project_busticket.model.Ticket;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
        SELECT t
        FROM Ticket t
        JOIN FETCH t.trip tr
        JOIN FETCH tr.route r
        JOIN FETCH tr.bus b
        JOIN FETCH t.seat s
        WHERE t.code = :code
        AND t.phone = :phone
    """)
    Optional<Ticket> findTicketDetail(
            @Param("code") String code,
            @Param("phone") String phone
    );

    List<Ticket> findByPhone(String phone);

    List<Ticket> findByStatus(TicketStatus status);

    @Query("""
        SELECT t
        FROM Ticket t
        WHERE t.status = :status
        AND t.createdAt < :limit
    """)
    List<Ticket> findExpiredTickets(
            @Param("status") TicketStatus status,
            @Param("limit") LocalDateTime limit
    );
}