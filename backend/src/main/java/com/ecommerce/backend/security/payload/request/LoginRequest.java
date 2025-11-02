package com.ecommerce.backend.security.payload.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for user sign-in requests.
 */
public class LoginRequest {

    @NotBlank(message = "Username (email) is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    // --- Constructors ---

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // --- Getters and Setters ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
