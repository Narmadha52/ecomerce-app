package com.ecommerce.backend.dto;

import com.ecommerce.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Data Transfer Object for transferring User data.
 * Used primarily for exposing user details (excluding the password) to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    // The previous compilation errors were caused by missing the import for this class:
    private Set<Role> roles;
}
