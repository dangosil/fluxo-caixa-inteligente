package com.dangosil.cashflow.category.service;

import com.dangosil.cashflow.category.dto.CategoryRequest;
import com.dangosil.cashflow.category.dto.CategoryResponse;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.category.repository.CategoryRepository;
import com.dangosil.cashflow.shared.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll(CategoryType type, Boolean active) {
        return findCategories(type, active).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(UUID id) {
        return categoryRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        Category category = new Category(request.name(), request.type());
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(UUID id, CategoryRequest request) {
        Category category = getCategory(id);
        category.update(request.name(), request.type());
        return toResponse(category);
    }

    @Transactional
    public void delete(UUID id) {
        Category category = getCategory(id);
        category.deactivate();
    }

    private List<Category> findCategories(CategoryType type, Boolean active) {
        if (type != null && active != null) {
            return categoryRepository.findByTypeAndActiveOrderByNameAsc(type, active);
        }
        if (type != null) {
            return categoryRepository.findByTypeOrderByNameAsc(type);
        }
        if (active != null) {
            return categoryRepository.findByActiveOrderByNameAsc(active);
        }
        return categoryRepository.findAllByOrderByNameAsc();
    }

    private Category getCategory(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
