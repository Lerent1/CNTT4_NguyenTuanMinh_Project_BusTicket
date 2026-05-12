package org.example.project_busticket.service;

import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Bus;
import org.example.project_busticket.repository.BusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;

    public List<Bus> getAll() {
        return busRepository.findAll();
    }

    public void create(Bus bus) {
        busRepository.save(bus);
    }

    public void delete(Long id) {
        busRepository.deleteById(id);
    }

    public Bus getById(Long id) {
        return busRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay xe"));
    }

//    public Bus getById(Long id) {
//        return busRepository.getBusById(id);
//    }

    public void update(Bus bus) {
        Bus existing = busRepository.findById(bus.getId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay xe"));

        existing.setLicensePlate(bus.getLicensePlate());
        existing.setBusType(bus.getBusType());
        existing.setTotalSeats(bus.getTotalSeats());
        existing.setCompany(bus.getCompany());
        existing.setDriverName(bus.getDriverName());

        busRepository.save(existing);
    }
}
