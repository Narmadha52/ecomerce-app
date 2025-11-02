package com.ecommerce.backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Extends JpaRepository to provide basic CRUD functionality for the User model.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User by their unique username.
     * @param username The username to search for.
     * @return An Optional containing the User if found, or empty otherwise.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a User with the given username already exists.
     * @param username The username to check.
     * @return true if the username exists, false otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a User with the given email already exists.
     * @param email The email to check.
     * @return true if the email exists, false otherwise.
     */
    Boolean existsByEmail(String email);
}
