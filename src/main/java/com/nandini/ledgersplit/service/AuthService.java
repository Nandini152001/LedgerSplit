package com.nandini.ledgersplit.service;

import com.nandini.ledgersplit.dto.RegistrationRequestDTO;
import com.nandini.ledgersplit.model.User;
import com.nandini.ledgersplit.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegistrationRequestDTO request) {

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());

        String passwordHash =
                passwordEncoder.encode(request.password());

        user.setPasswordHash(passwordHash);

        user.setRole("USER");

        return userRepository.save(user);
    }
}