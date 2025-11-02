package com.ecommerce.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for handling profile update requests.
 * Fields are optional and validated only if present.
 */
@Data
@NoArgsConstructor
public class UserUpdateRequest {
    @Size(min = 3, max = 20)
    private String username;

    @Size(max = 50)
    @Email
    private String email;

    private String firstName;
    private String lastName;

    // Note: Password updates typically use a separate DTO for current_password verification.
}
