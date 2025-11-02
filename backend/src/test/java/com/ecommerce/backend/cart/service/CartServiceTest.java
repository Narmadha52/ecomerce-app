// src/test/java/com/ecommerce/backend/cart/service/CartServiceTest.java

package com.ecommerce.backend.cart.service;

import com.ecommerce.backend.cart.dto.CartItemRequestDto;
import com.ecommerce.backend.cart.model.Cart;
import com.ecommerce.backend.cart.model.CartItem;
import com.ecommerce.backend.cart.repository.CartItemRepository;
import com.ecommerce.backend.cart.repository.CartRepository;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; // Added for correct List creation
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private Product testProduct;
    private Cart testCart;
    private String username = "testuser";

    @BeforeEach
    void setUp() {
        // Mock Security Context (User Login)
        testUser = new User();
        testUser.setUsername(username);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // Mock Product
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("50.00"));
        testProduct.setStockQuantity(5);

        // Mock Cart
        testCart = new Cart();
        testCart.setId(1L);
        testCart.setUser(testUser);
        testCart.setItems(new ArrayList<>());
    }

    @Test
    void addProductToCart_AddNewItem_Success() {
        // ARRANGE: FIX DTO Instantiation
        CartItemRequestDto request = new CartItemRequestDto();
        request.setProductId(1L);
        request.setQuantity(1);

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.findByCartAndProduct(any(Cart.class), any(Product.class)))
                .thenReturn(Optional.empty()); // Item does not exist

        // Mock the cart item repository to return the new item list for total calculation
        when(cartItemRepository.findByCart(testCart)).thenReturn(
                List.of(new CartItem(null, testCart, testProduct, 1))); // Use List.of for concise list creation

        // ACT
        Cart result = cartService.addProductToCart(request);

        // ASSERT
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        assertEquals(50.00, result.getTotalAmount()); // 1 * 50.00
    }

    @Test
    void addProductToCart_UpdateExistingItem_Success() {
        // ARRANGE: Item already exists with quantity 3
        CartItem existingItem = new CartItem(null, testCart, testProduct, 3);

        // FIX DTO Instantiation
        CartItemRequestDto request = new CartItemRequestDto();
        request.setProductId(1L);
        request.setQuantity(2); // Adding 2 more

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.findByCartAndProduct(any(Cart.class), any(Product.class)))
                .thenReturn(Optional.of(existingItem)); // Item exists

        // Mock total calculation: 3 + 2 = 5 items
        when(cartItemRepository.findByCart(testCart)).thenReturn(
                List.of(new CartItem(null, testCart, testProduct, 5)));

        // ACT
        Cart result = cartService.addProductToCart(request);

        // ASSERT
        // Quantity should be updated from 3 to 5
        assertEquals(5, existingItem.getQuantity());
        verify(cartItemRepository, times(1)).save(existingItem);
        assertEquals(250.00, result.getTotalAmount()); // 5 * 50.00
    }

    @Test
    void addProductToCart_InsufficientStock_ThrowsException() {
        // ARRANGE: Only 5 in stock, request is for 6
        CartItemRequestDto request = new CartItemRequestDto();
        request.setProductId(1L);
        request.setQuantity(6);

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartService.addProductToCart(request));

        assertTrue(exception.getMessage().contains("Insufficient stock"));

        // Ensure no save operation was attempted
        verify(cartItemRepository, never()).save(any());
    }

    // --- FIX: Tests were moved here to correct scoping ---

    @Test
    void updateCartItemQuantity_ToNewValue_Success() {
        // ARRANGE: Item exists with ID 10 and current quantity 2
        CartItem itemToUpdate = new CartItem(10L, testCart, testProduct, 2);
        when(cartItemRepository.findById(10L)).thenReturn(Optional.of(itemToUpdate));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart)); // Cart matches

        // Mock total calculation for new quantity 4
        when(cartItemRepository.findByCart(testCart)).thenReturn(
                List.of(new CartItem(10L, testCart, testProduct, 4)));

        // ACT
        Cart result = cartService.updateCartItemQuantity(10L, 4); // Update to 4

        // ASSERT
        verify(cartItemRepository, times(1)).save(itemToUpdate);
        assertEquals(4, itemToUpdate.getQuantity()); // Quantity updated
        assertEquals(200.00, result.getTotalAmount()); // 4 * 50.00
    }

    @Test
    void updateCartItemQuantity_ToZero_RemovesItem() {
        // ARRANGE: Item exists with ID 10
        CartItem itemToRemove = new CartItem(10L, testCart, testProduct, 2);
        when(cartItemRepository.findById(10L)).thenReturn(Optional.of(itemToRemove));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));

        // Mock total calculation for quantity 0 (empty cart)
        when(cartItemRepository.findByCart(testCart)).thenReturn(Collections.emptyList());

        // ACT
        cartService.updateCartItemQuantity(10L, 0); // Update to 0

        // ASSERT
        verify(cartItemRepository, times(1)).delete(itemToRemove);
    }

    @Test
    void updateCartItemQuantity_AccessDenied_ThrowsException() {
        // ARRANGE: Create an item belonging to a DIFFERENT cart
        Cart otherCart = new Cart();
        otherCart.setId(99L);
        CartItem itemToUpdate = new CartItem(10L, otherCart, testProduct, 2);

        when(cartItemRepository.findById(10L)).thenReturn(Optional.of(itemToUpdate));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart)); // Current user's cart is ID 1

        // ACT & ASSERT
        // Expect failure because itemToUpdate.cart.id (99) != testCart.id (1)
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartService.updateCartItemQuantity(10L, 1));

        assertTrue(exception.getMessage().contains("Access denied"));
    }
}