package org.example.project_busticket.service;

import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Trip;
import org.example.project_busticket.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    // tìm chuyến theo điểm đi, điểm đến, ngày
    public List<Trip> findTrips(
            Integer departureId,
            Integer destinationId,
            LocalDate date
    ) {

        return tripRepository.findTrips(departureId, destinationId, date);
    }

    // tìm theo id
    public Trip findById(Integer id) {

        return tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến"));
    }
}