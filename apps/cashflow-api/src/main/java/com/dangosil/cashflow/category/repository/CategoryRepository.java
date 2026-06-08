package com.dangosil.cashflow.category.repository;

import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findAllByOrderByNameAsc();

    List<Category> findByTypeOrderByNameAsc(CategoryType type);

    List<Category> findByActiveOrderByNameAsc(boolean active);

    List<Category> findByTypeAndActiveOrderByNameAsc(CategoryType type, boolean active);
}
