package com.ecommerce.backend.order.payload;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for placing a new order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    // This assumes the user ID is retrieved from the authenticated session,
    // so it only needs the shipping address details.
    private String shippingAddress;
}
