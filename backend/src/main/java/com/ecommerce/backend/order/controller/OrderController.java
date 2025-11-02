// src/main/java/com/ecommerce/backend/order/controller/OrderController.java

package com.ecommerce.backend.order.controller;

import com.ecommerce.backend.order.model.Order;
import com.ecommerce.backend.order.model.OrderStatus;
import com.ecommerce.backend.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // --- CUSTOMER ENDPOINTS ---

    /**
     * POST /api/orders/checkout: Converts the cart into a permanent order.
     * Requires customer authentication.
     */
    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Order> checkout(@RequestParam String shippingAddress) {
        try {
            Order order = orderService.placeOrderFromCart(shippingAddress);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Catches exceptions like "Cart is empty" or "Insufficient stock"
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET /api/orders/history: Retrieves all orders for the current user.
     * Requires customer authentication.
     */
    @GetMapping("/history")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public List<Order> getOrderHistory() {
        return orderService.getOrderHistory();
    }

    // --- ADMIN ENDPOINTS ---

    /**
     * GET /api/orders (Admin): Retrieves all orders.
     * FIX: Calls the new public service method (getAllOrdersAdmin()).
     * Requires ADMIN role.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Order> getAllOrders() {
        // CORRECT: Delegates the call to the Service Layer
        return orderService.getAllOrdersAdmin();
    }

    /**
     * PUT /api/orders/{id}/status: Updates the status of a specific order.
     * Requires ADMIN role.
     */
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}