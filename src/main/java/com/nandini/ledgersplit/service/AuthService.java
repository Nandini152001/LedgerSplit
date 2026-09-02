package com.nandini.ledgersplit.service;

import com.nandini.ledgersplit.dto.LoginRequestDTO;
import com.nandini.ledgersplit.dto.LoginResponseDTO;
import com.nandini.ledgersplit.dto.RegistrationRequestDTO;
import com.nandini.ledgersplit.model.User;
import com.nandini.ledgersplit.repository.UserRepository;
import com.nandini.ledgersplit.security.JWTService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JWTService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

public LoginResponseDTO logIn(LoginRequestDTO loginRequestDTO){
        User user = userRepository.findByEmail(loginRequestDTO.email()).orElseThrow(() -> new RuntimeException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(
                loginRequestDTO.password(),
                user.getPasswordHash()
        );

        if(!passwordMatches){
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponseDTO(token);
}
}