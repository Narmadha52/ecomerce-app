// src/main/java/com/ecommerce/backend/payment/dto/PaymentOrderDto.java

package com.ecommerce.backend.payment.dto;

import lombok.Data;
import java.math.BigDecimal;

// DTO used for the client to initiate the payment
@Data
public class PaymentOrderDto {
    private Long orderId;
    private BigDecimal amount;
    private String currency = "INR";
}