package com.ecommerce.backend.security.controller;

import com.ecommerce.backend.security.payload.request.LoginRequest;
import com.ecommerce.backend.security.payload.request.SignupRequest;
import com.ecommerce.backend.security.services.AuthService;
import com.ecommerce.backend.security.payload.response.JwtResponse;
import com.ecommerce.backend.payload.response.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling user authentication (login and registration).
 * Base path: /api/auth
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Handles user login request.
     * @param loginRequest The user's username and password.
     * @return ResponseEntity containing the JWT and user details.
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Handles user registration request.
     * @param signupRequest The new user's details.
     * @return ResponseEntity containing a success message.
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        String message = authService.registerUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse(message));
    }
}
