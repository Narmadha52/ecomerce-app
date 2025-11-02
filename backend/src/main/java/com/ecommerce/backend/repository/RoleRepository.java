package com.ecommerce.backend.user.repository;

import com.ecommerce.backend.user.model.ERole;
import com.ecommerce.backend.user.model.Role; // FIX: Correct Role model import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Role entity operations.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole name);
}
