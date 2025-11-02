package com.ecommerce.backend.user.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

/**
 * Data Transfer Object for User details.
 * Used for returning user information from the API endpoints.
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
    // This will hold the simplified roles (e.g., "USER", "ADMIN")
    private Set<String> roles;
    private String address;
}
