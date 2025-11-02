// src/main/java/com/ecommerce/backend/category/repository/CategoryRepository.java

package com.ecommerce.backend.repository;

import com.ecommerce.backend.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
