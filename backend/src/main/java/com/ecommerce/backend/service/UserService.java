package com.ecommerce.backend.service;

import java.util.List;

/**
 * Defines the business logic contract for managing User entities.
 */
public interface UserService {

    /**
     * Retrieves all users from the system.
     * @return A list of all User entities.
     */
    List<User> getAllUsers();

    /**
     * Retrieves a single user by their ID.
     * @param id The ID of the user to retrieve.
     * @return The User entity.
     * @throws com.ecommerce.backend.exception.ResourceNotFoundException if user not found.
     */
    User getUserById(Long id);

    /**
     * Updates an existing user's details.
     * @param id The ID of the user to update.
     * @param userDetails The User object containing the new details (Note: in a real app, use a DTO).
     * @return The updated User entity.
     */
    User updateUser(Long id, User userDetails);

    /**
     * Deletes a user by their ID.
     * @param id The ID of the user to delete.
     * @throws com.ecommerce.backend.exception.ResourceNotFoundException if user not found.
     */
    void deleteUser(Long id);
}
