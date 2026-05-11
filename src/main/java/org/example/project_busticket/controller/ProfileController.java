package org.example.project_busticket.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.User;
import org.example.project_busticket.model.UserProfiles;
import org.example.project_busticket.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {

        String username = (String) session.getAttribute("currentUser");

        if (username == null) {
            return "redirect:/auth/login";
        }

        User user = profileService.getCurrentUser(username);

        model.addAttribute("user", user);

        return "profile";
    }

    @PostMapping("/profile/update")
    public String update(
            @Valid @ModelAttribute("profile") UserProfiles profile,
            BindingResult result,
            HttpSession session,
            Model model
    ) {

        if (result.hasErrors()) {
            model.addAttribute("user",
                    profileService.getCurrentUser(
                            (String) session.getAttribute("currentUser")
                    ));

            return "profile";
        }

        String username = (String) session.getAttribute("currentUser");

        profileService.updateProfile(username, profile);

        return "redirect:/profile";
    }
}