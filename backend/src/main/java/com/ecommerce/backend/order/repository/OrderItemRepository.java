package com.ecommerce.backend.order.repository;

import com.ecommerce.backend.order.model.Order;
import com.ecommerce.backend.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing OrderItem entities.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}
