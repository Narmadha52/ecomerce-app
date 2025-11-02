package com.ecommerce.backend.order.service;

import com.ecommerce.backend.order.model.OrderStatus;
import com.ecommerce.backend.order.payload.CheckoutRequest;
import com.ecommerce.backend.order.payload.OrderDto;
import java.util.List;

/**
 * Interface for managing order-related business logic.
 * This contract defines all operations available for orders.
 */
public interface OrderService {

    /**
     * Places a new order based on the current cart content.
     */
    OrderDto placeOrder(Long userId, CheckoutRequest checkoutRequest);

    /**
     * Retrieves an order by its ID.
     */
    OrderDto getOrderById(Long orderId);

    /**
     * Retrieves all orders for a specific user.
     */
    List<OrderDto> getUserOrders(Long userId);

    /**
     * Updates the status of an order.
     */
    OrderDto updateOrderStatus(Long orderId, OrderStatus newStatus);
}
