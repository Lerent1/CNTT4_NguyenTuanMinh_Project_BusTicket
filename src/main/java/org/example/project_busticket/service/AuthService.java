package org.example.project_busticket.service;

import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.enums.Role;
import org.example.project_busticket.model.User;
import org.example.project_busticket.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // ================= LOGIN =================
    public User login(String username, String password) {

        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) return null;

        if (!passwordEncoder.matches(password, user.getPasswordHash()))
            return null;

        return user;
    }

    // ================= REGISTER PASSENGER =================
    public void registerPassenger(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username da ton tai");
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setRole(Role.PASSENGER);

        if (user.getProfile() != null) {
            user.getProfile().setUser(user);
        }

        userRepository.save(user);
    }

    // ================= CREATE STAFF =================
    public void createStaff(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username da ton tai");
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setRole(Role.STAFF);

        if (user.getProfile() != null) {
            user.getProfile().setUser(user);
        }

        userRepository.save(user);
    }
}