package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order entities.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds all orders placed by a specific user.
     * @param user The User entity to find orders for.
     * @return A list of orders associated with the user.
     */
    List<Order> findByUser(User user);

    /**
     * Finds an order by its ID and ensures it belongs to the specified user.
     * @param id The ID of the order.
     * @param user The User entity who placed the order.
     * @return An Optional containing the order if found, or empty.
     */
    Optional<Order> findByIdAndUser(Long id, User user);
}
