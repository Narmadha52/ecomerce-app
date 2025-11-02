// src/main/java/com/ecommerce/backend/payment/dto/PaymentVerificationDto.java

package com.ecommerce.backend.dto;

import lombok.Data;

// DTO used for the client to send payment confirmation details back to the server
@Data
public class PaymentVerificationDto {
    private Long orderId;
    private String paymentGatewayOrderId;
    private String paymentGatewayPaymentId;
    private String paymentSignature; // The hashed signature for verification
}
