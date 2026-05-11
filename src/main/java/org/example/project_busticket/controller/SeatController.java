package org.example.project_busticket.controller;

import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Seat;
import org.example.project_busticket.service.SeatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/{tripId}")
    public String seatMap(
            @PathVariable Integer tripId,
            Model model
    ) {

        List<Seat> seats =
                seatService.getSeatsByTrip(tripId);

        model.addAttribute("seats", seats);

        model.addAttribute("tripId", tripId);

        return "seatList";
    }

    @PostMapping("/hold/{seatId}")
    public String holdSeat(
            @PathVariable Long seatId,
            @RequestParam Integer tripId
    ) {

        seatService.holdSeat(seatId);

        return "redirect:/seats/" + tripId;
    }
}