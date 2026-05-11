package org.example.project_busticket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Bus;
import org.example.project_busticket.service.BusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/buses")
@RequiredArgsConstructor
public class BusController {
    private final BusService busService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("buses", busService.getAll());
        model.addAttribute("bus", new Bus());
        return "admin/buses";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("bus", new Bus());
        return "admin/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("bus") Bus bus,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {
            model.addAttribute("buses", busService.getAll());
            return "admin/buses";
        }

        busService.create(bus);

        return "redirect:/admin/buses";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model
    ) {

        model.addAttribute("bus", busService.getById(id));
        model.addAttribute("buses", busService.getAll());
        return "admin/form";
    }

    @PostMapping("/update")
    public String update(
            @Valid @ModelAttribute("bus") Bus bus,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {

            model.addAttribute("buses", busService.getAll());

            return "admin/buses";
        }

        busService.update(bus);

        return "redirect:/admin/buses";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        busService.delete(id);

        return "redirect:/admin/buses";
    }
}