package org.example.project_busticket.controller;

import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Trip;
import org.example.project_busticket.service.LocationService;
import org.example.project_busticket.service.SeatService;
import org.example.project_busticket.service.TripService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TripController {

    private final LocationService locationService;
    private final TripService tripService;
    private final SeatService seatService;

    // PAGE SEARCH
    @GetMapping("/search-trip")
    public String searchTripPage(Model model) {
        model.addAttribute("locations", locationService.findAll());
        return "searchTrip";
    }

    // SEARCH RESULT
    @GetMapping("/search-trip/result")
    public String searchTripResult(

            @RequestParam(required = false)
            Integer departureId,

            @RequestParam(required = false)
            Integer destinationId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            Model model
    ) {

        // load lại locations
        model.addAttribute("locations", locationService.findAll());

        // VALIDATION
        boolean hasError = false;

        if (departureId == null) {
            model.addAttribute("departureError", "Vui lòng chọn điểm đi");
            hasError = true;
        }

        if (destinationId == null) {
            model.addAttribute("destinationError", "Vui lòng chọn điểm đến");
            hasError = true;
        }

        if (date == null) {
            model.addAttribute("dateError", "Vui lòng chọn ngày đi");
            hasError = true;
        }

        // điểm đi và điểm đến giống nhau
        if (departureId != null
                && destinationId != null
                && departureId.equals(destinationId)) {

            model.addAttribute("sameLocationError",
                    "Điểm đi và điểm đến không được giống nhau"
            );

            hasError = true;
        }

        // nếu có lỗi
        if (hasError) {
            return "searchTrip";
        }

        // tìm chuyến
        List<Trip> trips = tripService.findTrips(departureId, destinationId, date
        );

        model.addAttribute("trips", trips);

        model.addAttribute("selectedDate", date);

        // không có chuyến
        if (trips.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy chuyến xe phù hợp");
        }
        return "searchTrip";
    }

    // LOAD SEAT
    @GetMapping("/trip/{tripId}/seats")
    public String showSeats(
            @PathVariable Integer tripId,
            Model model
    ) {

        Trip trip = tripService.findById(tripId);

        model.addAttribute("trip", trip);
        model.addAttribute("tripId", tripId);
        model.addAttribute("seats", seatService.getSeatsByTrip(tripId));

        return "seatList";
    }
}