package com.ecommerce.backend.product.service;

import com.ecommerce.backend.product.model.Product;
import com.ecommerce.backend.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Interface for the Product Service, defining all business logic operations related to products.
 */
public interface ProductService {

    /**
     * Retrieves all products, optionally filtered by category ID.
     * @param categoryId The ID of the category to filter by (or null for all products).
     * @return A list of Product entities.
     */
    List<Product> findAllProducts(Long categoryId);

    /**
     * Retrieves a single product by its ID.
     * @param id The ID of the product.
     * @return The Product entity.
     * @throws ResourceNotFoundException if the product is not found.
     */
    Product findProductById(Long id) throws ResourceNotFoundException;

    /**
     * Creates a new product.
     * @param product The Product entity to create.
     * @return The saved Product entity, including the generated ID.
     */
    Product createProduct(Product product);

    /**
     * Updates an existing product.
     * @param id The ID of the product to update.
     * @param productDetails The Product entity containing updated details.
     * @return The updated Product entity.
     * @throws ResourceNotFoundException if the product is not found.
     */
    Product updateProduct(Long id, Product productDetails) throws ResourceNotFoundException;

    /**
     * Deletes a product by its ID.
     * @param id The ID of the product to delete.
     * @throws ResourceNotFoundException if the product is not found.
     */
    void deleteProduct(Long id) throws ResourceNotFoundException;
}
