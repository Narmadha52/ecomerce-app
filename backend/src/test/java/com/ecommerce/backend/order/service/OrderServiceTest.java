// src/test/java/com/ecommerce/backend/order/service/OrderServiceTest.java

package com.ecommerce.backend.order.service;

import com.ecommerce.backend.cart.model.Cart;
import com.ecommerce.backend.cart.model.CartItem;
import com.ecommerce.backend.cart.repository.CartRepository;
import com.ecommerce.backend.order.model.Order;
import com.ecommerce.backend.order.model.OrderStatus;
import com.ecommerce.backend.order.repository.OrderItemRepository;
import com.ecommerce.backend.order.repository.OrderRepository;
import com.ecommerce.backend.product.model.Product;
import com.ecommerce.backend.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Initializes Mockito annotations
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;

    // InjectMocks creates a real instance of OrderService and injects the Mocks above
    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private Cart testCart;
    private Product testProduct;
    private CartItem testCartItem;
    private String username = "testuser";

    @BeforeEach
    void setUp() {
        // 1. Mock the Security Context to simulate a logged-in user
        testUser = new User();
        testUser.setUsername(username);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // 2. Mock Product data
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Laptop");
        testProduct.setPrice(new BigDecimal("1000.00"));
        testProduct.setStockQuantity(10);

        // 3. Mock Cart Item data
        testCartItem = new CartItem();
        testCartItem.setProduct(testProduct);
        testCartItem.setQuantity(2); // Quantity to purchase

        // 4. Mock Cart data
        testCart = new Cart();
        testCart.setId(100L);
        testCart.setUser(testUser);
        testCart.setItems(Collections.singletonList(testCartItem));
        testCartItem.setCart(testCart);

        // 5. Mock Repositories
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));

        // Mock saving the new Order (important for transactional services)
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L); // Simulate ID generation
            return order;
        });
    }

    @Test
    void placeOrderFromCart_Success() {
        // ACT
        Order result = orderService.placeOrderFromCart("123 Test St.");

        // ASSERT 1: Verify Order Details
        assertNotNull(result);
        assertEquals(OrderStatus.PENDING_PAYMENT, result.getStatus());
        assertEquals(new BigDecimal("2000.00"), result.getTotalAmount()); // 2 * 1000.00

        // ASSERT 2: Verify Product Stock Update
        assertEquals(8, testProduct.getStockQuantity()); // Stock should be 10 - 2 = 8
        verify(productRepository, times(1)).save(testProduct);

        // ASSERT 3: Verify Persistence
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());

        // ASSERT 4: Verify Cart Cleanup
        verify(cartRepository, times(1)).delete(testCart);
    }

    @Test
    void placeOrderFromCart_InsufficientStock_ThrowsException() {
        // ARRANGE: Set stock lower than purchase quantity
        testProduct.setStockQuantity(1);

        // ACT & ASSERT
        // Expect a RuntimeException due to the stock check
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.placeOrderFromCart("123 Test St."));

        assertTrue(exception.getMessage().contains("Insufficient stock"));

        // ASSERT 3: Verify No Persistence or Stock Change Occurred
        // Crucial check: if an exception is thrown, nothing should have been saved.
        verify(productRepository, never()).save(any(Product.class));
        verify(orderRepository, never()).save(any(Order.class));
        verify(cartRepository, never()).delete(any(Cart.class));
    }

    @Test
    void placeOrderFromCart_EmptyCart_ThrowsException() {
        // ARRANGE: Mock an empty cart
        testCart.setItems(Collections.emptyList());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.placeOrderFromCart("123 Test St."));

        assertTrue(exception.getMessage().contains("Cart is empty"));
    }
}