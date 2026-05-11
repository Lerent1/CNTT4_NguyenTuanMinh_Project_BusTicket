package org.example.project_busticket.repository;

import org.example.project_busticket.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Integer> {

    @Query("""
    SELECT t FROM Trip t
    WHERE t.route.departure.id = :departureId
    AND t.route.destination.id = :destinationId
    AND DATE(t.departureTime) = :date
    """)
    List<Trip> findTrips(@Param("departureId") Integer departureId,
                         @Param("destinationId") Integer destinationId,
                         @Param("date") LocalDate date);
}
