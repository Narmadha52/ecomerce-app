package com.ecommerce.backend.category.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.ecommerce.backend.category.repository.CategoryRepository;
import com.ecommerce.backend.category.model.Category;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override public List<Category> findAll() { return categoryRepository.findAll(); }
    @Override public Category findById(Integer id) { return categoryRepository.findById(id).orElse(null); }
    @Override public Category save(Category c) { return categoryRepository.save(c); }
    @Override public void deleteById(Integer id) { categoryRepository.deleteById(id); }
}
