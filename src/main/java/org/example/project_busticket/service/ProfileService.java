package org.example.project_busticket.service;

import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.User;
import org.example.project_busticket.model.UserProfiles;
import org.example.project_busticket.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    public void updateProfile(String username, UserProfiles data) {
        User user = userRepository.findByUsername(username)
                .orElseThrow();

        UserProfiles profile = user.getProfile();

        if (profile == null) {
            profile = new UserProfiles();
            profile.setUser(user);
        }

        profile.setFullName(data.getFullName());
        profile.setPhone(data.getPhone());
        profile.setEmail(data.getEmail());
        profile.setAddress(data.getAddress());

        user.setProfile(profile);

        userRepository.save(user);

    }

    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow();
    }
}
