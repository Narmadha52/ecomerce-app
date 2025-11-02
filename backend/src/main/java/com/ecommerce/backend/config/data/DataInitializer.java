package com.ecommerce.backend.config.data;

import com.ecommerce.backend.user.model.Role;
import com.ecommerce.backend.user.model.ERole;
import com.ecommerce.backend.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

/**
 * Initializes the database with essential user roles when the application starts.
 * This class runs once upon application startup to ensure the foundational roles
 * (ADMIN, MODERATOR, USER) are present in the 'roles' table.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize all required roles
        initializeRole(ERole.ROLE_ADMIN);
        initializeRole(ERole.ROLE_MODERATOR);
        initializeRole(ERole.ROLE_USER);

        System.out.println("Initial Roles confirmed and loaded: ROLE_ADMIN, ROLE_MODERATOR, and ROLE_USER.");
    }

    /**
     * Checks if a role exists by name and creates it if it does not.
     * @param roleName The ERole enum value to check/create.
     */
    private void initializeRole(ERole roleName) {
        // Check if the role already exists in the database
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role newRole = new Role();
            newRole.setName(roleName);
            // Save the new role
            roleRepository.save(newRole);
            System.out.println("Created initial role: " + roleName);
        }
    }
}
