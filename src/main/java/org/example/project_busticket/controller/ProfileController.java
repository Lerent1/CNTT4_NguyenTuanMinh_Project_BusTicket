package org.example.project_busticket.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project_busticket.dto.UserProfileDTO;
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

        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            return "redirect:/auth/login";
        }

        User fullUser = profileService.getCurrentUser(user.getUsername());

        model.addAttribute("user", fullUser);

        UserProfileDTO dto = new UserProfileDTO();

        if (fullUser.getProfile() != null) {
            dto.setFullName(fullUser.getProfile().getFullName());
            dto.setPhone(fullUser.getProfile().getPhone());
            dto.setEmail(fullUser.getProfile().getEmail());
            dto.setAddress(fullUser.getProfile().getAddress());
        }

        model.addAttribute("profile", dto);

        return "profile";
    }

    @PostMapping("/profile/update")
    public String update(
            @Valid @ModelAttribute("profile") UserProfileDTO dto,
            BindingResult result,
            HttpSession session,
            Model model
    ) {

        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            return "redirect:/auth/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("user", profileService.getCurrentUser(user.getUsername()));
            return "profile";
        }

        profileService.updateProfile(user.getUsername(), dto);

        return "redirect:/profile";
    }
}