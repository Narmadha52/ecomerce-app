package com.ecommerce.backend.cart.service;

import com.ecommerce.backend.cart.model.Cart;
import com.ecommerce.backend.cart.model.CartItem;
import com.ecommerce.backend.cart.repository.CartItemRepository;
import com.ecommerce.backend.cart.repository.CartRepository;
import com.ecommerce.backend.product.model.Product;
import com.ecommerce.backend.product.repository.ProductRepository;
import com.ecommerce.backend.user.repository.UserRepository;
import com.ecommerce.backend.exception.ResourceNotFoundException; // Assuming this exists
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository; // Product Repository is necessary here

    /**
     * Helper method to fetch the user's cart, or create a new one if it doesn't exist.
     */
    @Override
    public Cart getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for ID: " + userId));

        // Attempt to find existing cart, otherwise create a new one
        return cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
    }

    @Transactional
    @Override
    public Cart addToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        // Check if the item already exists in the cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(cartItemRepository.save(newItem));
        }

        // Update total amount and save the cart
        cart.setTotalAmount(calculateTotal(userId));
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public Cart updateCartItemQuantity(Long userId, Long productId, int quantity) {
        Cart cart = getCartByUserId(userId);

        CartItem itemToUpdate = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart with ID: " + productId));

        if (quantity <= 0) {
            // Remove item if the new quantity is zero or less
            cart.getItems().remove(itemToUpdate);
            cartItemRepository.delete(itemToUpdate);
        } else {
            itemToUpdate.setQuantity(quantity);
            cartItemRepository.save(itemToUpdate);
        }

        // Update total amount and save the cart
        cart.setTotalAmount(calculateTotal(userId));
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void removeCartItem(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart with ID: " + productId));

        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);

        // Update total amount and save the cart
        cart.setTotalAmount(calculateTotal(userId));
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        // Delete all associated items
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();

        // Reset total amount
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    @Override
    public BigDecimal calculateTotal(Long userId) {
        Cart cart = getCartByUserId(userId);

        // Calculate total by multiplying each item's price by its quantity and summing them up
        return cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Creates and persists a new, empty cart for a given user.
     */
    private Cart createNewCart(User user) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setTotalAmount(BigDecimal.ZERO);
        return cartRepository.save(newCart);
    }
}
