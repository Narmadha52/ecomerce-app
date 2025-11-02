package com.ecommerce.backend.util;

import com.ecommerce.backend.product.model.Product;
import com.ecommerce.backend.product.repository.ProductRepository;
import com.ecommerce.backend.user.model.ERole;
import com.ecommerce.backend.user.repository.RoleRepository;
import com.ecommerce.backend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Component to load initial data (Roles, Admin User, Products) into the database
 * on application startup, enabling quick testing.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Starting DataLoader: Checking database for initial setup...");

        createRolesIfNotExist();
        createInitialAdminUserIfNotExist();
        createSampleProductsIfNotExist();

        logger.info("DataLoader completed successfully.");
    }

    private void createRolesIfNotExist() {
        for (ERole roleName : ERole.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                roleRepository.save(new Role(roleName));
                logger.info("Created role: {}", roleName);
            }
        }
    }

    private void createInitialAdminUserIfNotExist() {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roleRepository.findByName(ERole.ROLE_ADMIN).ifPresent(roles::add);

            User admin = new User(
                    "AdminUser",
                    "admin@example.com",
                    encoder.encode("password123"), // Default password for testing
                    roles
            );
            userRepository.save(admin);
            logger.info("Created default ADMIN user: admin@example.com with password 'password123'");
        }
    }

    private void createSampleProductsIfNotExist() {
        if (productRepository.count() == 0) {

            // Create Product 1
            Product p1 = new Product();
            p1.setName("Wireless Noise-Cancelling Headphones");
            p1.setDescription("Premium over-ear headphones with 40-hour battery life and superior audio quality.");
            p1.setPrice(new BigDecimal("199.99"));
            p1.setStockQuantity(50);
            p1.setAvailable(true);
            productRepository.save(p1);

            // Create Product 2
            Product p2 = new Product();
            p2.setName("Mechanical Keyboard (TKL)");
            p2.setDescription("Tenkeyless mechanical keyboard with tactile brown switches. Great for typing and gaming.");
            p2.setPrice(new BigDecimal("99.50"));
            p2.setStockQuantity(20);
            p2.setAvailable(true);
            productRepository.save(p2);

            // Create Product 3 (Out of stock)
            Product p3 = new Product();
            p3.setName("4K Ultra HD Monitor");
            p3.setDescription("27-inch monitor with 144Hz refresh rate, ideal for creative professionals and gaming.");
            p3.setPrice(new BigDecimal("499.00"));
            p3.setStockQuantity(0); // Out of stock
            p3.setAvailable(false);
            productRepository.save(p3);

            logger.info("Created 3 sample products.");
        }
    }
}
