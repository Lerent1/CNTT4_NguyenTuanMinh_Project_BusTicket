package org.example.project_busticket.service;

import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Locations;
import org.example.project_busticket.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Locations> findAll() {
        return locationRepository.findAll();
    }
}