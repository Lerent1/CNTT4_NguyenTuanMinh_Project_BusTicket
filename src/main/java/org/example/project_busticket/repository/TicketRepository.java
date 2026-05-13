package org.example.project_busticket.repository;

import org.example.project_busticket.model.enums.TicketStatus;
import org.example.project_busticket.model.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
    SELECT t FROM Ticket t
    JOIN FETCH t.trip tr
    JOIN FETCH tr.bus b
    JOIN FETCH tr.route r
    WHERE t.code = :code AND t.phone = :phone
    """)
    Optional<Ticket> findTicketDetail(String code, String phone);

    List<Ticket> findByPhone(String phone);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByUser_Id(Long userId);

    //h3
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

    // H4
    @Query("""
    SELECT r.id,
           CONCAT(d.name, ' - ', des.name) AS routeName,
           SUM(t.totalPrice) AS revenue
    FROM Ticket t
    JOIN t.trip tr
    JOIN tr.route r
    JOIN r.departure d
    JOIN r.destination des
    WHERE t.status = 'PAID'
    GROUP BY r.id, d.name, des.name
    ORDER BY revenue DESC
    """)
    List<Object[]> getRevenueByRoute();

    @Query("""
    SELECT b.id, 
           b.licensePlate,
           SUM(t.totalPrice) AS revenue
    FROM Ticket t
    JOIN t.trip tr
    JOIN tr.bus b
    WHERE t.status = 'PAID'
    GROUP BY b.id, b.licensePlate
    ORDER BY revenue DESC
    """)
    List<Object[]> getTop5RevenueByBus(Pageable pageable);
}