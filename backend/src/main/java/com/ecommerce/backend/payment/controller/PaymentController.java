// src/main/java/com/ecommerce/backend/payment/controller/PaymentController.java

package com.ecommerce.backend.payment.controller;

import com.ecommerce.backend.order.model.Order;
import com.ecommerce.backend.payment.dto.PaymentOrderDto;
import com.ecommerce.backend.payment.dto.PaymentVerificationDto;
import com.ecommerce.backend.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@PreAuthorize("hasAuthority('CUSTOMER')")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 1. Initiates the payment process by creating an order with the payment gateway.
     * Returns the payment gateway's order ID.
     */
    @PostMapping("/create-order")
    public ResponseEntity<String> createPaymentOrder(@RequestBody PaymentOrderDto dto) {
        try {
            String pgOrderId = paymentService.createPaymentOrder(dto);
            return ResponseEntity.ok(pgOrderId);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 2. Finalizes the order after successful payment.
     * This is typically called by the frontend after the user completes payment.
     */
    @PostMapping("/verify")
    public ResponseEntity<Order> verifyPayment(@RequestBody PaymentVerificationDto dto) {
        try {
            Order finalizedOrder = paymentService.verifyPaymentAndFinalizeOrder(dto);
            return ResponseEntity.ok(finalizedOrder);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}