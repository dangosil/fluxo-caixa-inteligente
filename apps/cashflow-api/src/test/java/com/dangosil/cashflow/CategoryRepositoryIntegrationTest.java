package com.dangosil.cashflow;

import static org.assertj.core.api.Assertions.assertThat;

import com.dangosil.cashflow.cashentry.repository.CashEntryRepository;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.category.repository.CategoryRepository;
import com.dangosil.cashflow.cashexpense.repository.CashExpenseRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CategoryRepositoryIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CashEntryRepository cashEntryRepository;

    @Autowired
    private CashExpenseRepository cashExpenseRepository;

    @BeforeEach
    void setUp() {
        cashExpenseRepository.deleteAll();
        cashEntryRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldFindCategoriesByTypeAndActiveStatusOrderedByName() {
        Category sales = categoryRepository.save(new Category("Sales", CategoryType.INCOME));
        Category services = categoryRepository.save(new Category("Services", CategoryType.INCOME));
        Category inactiveIncome = new Category("Inactive income", CategoryType.INCOME);
        inactiveIncome.deactivate();
        categoryRepository.save(inactiveIncome);
        categoryRepository.save(new Category("Rent", CategoryType.EXPENSE));

        List<Category> categories = categoryRepository.findByTypeAndActiveOrderByNameAsc(CategoryType.INCOME, true);

        assertThat(categories)
                .extracting(Category::getId)
                .containsExactly(sales.getId(), services.getId());
    }
}
