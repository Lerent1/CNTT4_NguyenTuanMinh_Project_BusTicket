package org.example.project_busticket.config;

import lombok.RequiredArgsConstructor;
import org.example.project_busticket.model.Enums.Role;
import org.example.project_busticket.model.User;
import org.example.project_busticket.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("admin").isEmpty()) {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            User admin = new User();

            admin.setUsername("admin");

            admin.setPasswordHash(encoder.encode("123456"));

            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("ADMIN CREATED");
        }
    }
}