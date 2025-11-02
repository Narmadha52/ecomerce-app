package com.ecommerce.backend.user.controller;

import com.ecommerce.backend.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing user data.
 * All endpoints here are typically secured and require administrative roles.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Dependency injection via constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users in the system.
     * Requires 'ADMIN' authority.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a single user by their ID.
     * Can be accessed by 'ADMIN' or the user themselves (if ID matches).
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Updates an existing user's details.
     * Note: In a production application, you would use a DTO here instead of the full User entity.
     * Requires 'ADMIN' authority.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Deletes a user from the system.
     * Requires 'ADMIN' authority.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
