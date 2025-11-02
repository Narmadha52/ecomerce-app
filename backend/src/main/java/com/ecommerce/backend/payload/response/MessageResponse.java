package com.ecommerce.backend.payload.response;

/**
 * Generic DTO for sending simple messages back to the client (e.g., success messages).
 */
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    // Standard getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
