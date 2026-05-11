package org.example.project_busticket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.project_busticket.model.Enums.SeatStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "seats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    private LocalDateTime timeout;
}
