package com.ecommerce.backend.order.service;

import com.ecommerce.backend.cart.model.Cart;
import com.ecommerce.backend.cart.repository.CartRepository;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.order.model.Order;
import com.ecommerce.backend.order.model.OrderItem;
import com.ecommerce.backend.order.model.OrderStatus;
import com.ecommerce.backend.order.payload.CheckoutRequest;
import com.ecommerce.backend.order.repository.OrderItemRepository;
import com.ecommerce.backend.order.repository.OrderRepository;
import com.ecommerce.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    private static final String ORDER_NOT_FOUND_MSG = "Order not found with id: ";
    private static final String USER_NOT_FOUND_MSG = "User not found with id: ";

    /**
     * Creates a new Order from the user's cart items.
     * @param userId The ID of the user placing the order.
     * @param checkoutRequest The checkout details (e.g., shipping address).
     * @return The newly created Order entity.
     * @throws ResourceNotFoundException if the user or cart is not found.
     */
    @Override
    public Order createOrder(Long userId, CheckoutRequest checkoutRequest) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG + userId));

        // Assuming a user has one cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot create order from an empty cart.");
        }

        // 1. Create the Order
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setOrderDate(Instant.now());
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setShippingAddress(checkoutRequest.getShippingAddress());

        // Calculate total amount while creating OrderItems
        BigDecimal orderTotal = BigDecimal.ZERO;

        Set<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            // CRITICAL: Capture the price at the time of order
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());

            // Calculate item total and add to order total
            BigDecimal itemTotal = cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
            orderTotal.add(itemTotal);

            return orderItem;
        }).collect(Collectors.toSet());

        newOrder.setOrderItems(orderItems);
        newOrder.setTotalAmount(orderTotal);

        // 2. Save the Order (cascades save to OrderItems)
        Order savedOrder = orderRepository.save(newOrder);

        // 3. Clear the user's cart (Transactional cleanup)
        cartRepository.delete(cart);

        // NOTE: Stock reduction and payment processing would typically happen after this,
        // in a separate, dedicated step/service call (e.g., in PaymentService or a dedicated InventoryService).

        return savedOrder;
    }

    @Override
    public List<Order> findAllOrdersByUser(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG + userId));
        return orderRepository.findByUser(user);
    }

    @Override
    public Order findOrderById(Long orderId) throws ResourceNotFoundException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND_MSG + orderId));
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) throws ResourceNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND_MSG + orderId));

        order.setStatus(status);
        return orderRepository.save(order);
    }
}