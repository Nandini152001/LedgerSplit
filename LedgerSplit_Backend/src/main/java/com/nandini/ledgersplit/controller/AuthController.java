package com.nandini.ledgersplit.controller;

import com.nandini.ledgersplit.dto.LoginRequestDTO;
import com.nandini.ledgersplit.dto.LoginResponseDTO;
import com.nandini.ledgersplit.dto.RegistrationRequestDTO;
import com.nandini.ledgersplit.model.User;
import com.nandini.ledgersplit.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegistrationRequestDTO requestDTO){
        User user = authService.register(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request){
        return authService.logIn(request);
    }
}
