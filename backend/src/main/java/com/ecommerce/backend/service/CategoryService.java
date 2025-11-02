package com.ecommerce.backend.service;

import com.ecommerce.backend.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Integer id);
    Category save(Category category);
    void deleteById(Integer id);
}
