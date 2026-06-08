package com.dangosil.cashflow.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dangosil.cashflow.category.dto.CategoryRequest;
import com.dangosil.cashflow.category.dto.CategoryResponse;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.category.repository.CategoryRepository;
import com.dangosil.cashflow.shared.exception.ResourceNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldCreateValidCategory() {
        CategoryRequest request = new CategoryRequest("Sales", CategoryType.INCOME);
        Category savedCategory = new Category("Sales", CategoryType.INCOME);

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponse response = categoryService.create(request);

        assertThat(response.name()).isEqualTo("Sales");
        assertThat(response.type()).isEqualTo(CategoryType.INCOME);
        assertThat(response.active()).isTrue();
    }

    @Test
    void shouldThrowErrorWhenCategoryDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found");
    }
}
