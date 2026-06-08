package com.dangosil.cashflow;

import static org.assertj.core.api.Assertions.assertThat;

import com.dangosil.cashflow.cashentry.repository.CashEntryRepository;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.category.repository.CategoryRepository;
import com.dangosil.cashflow.cashexpense.entity.CashExpense;
import com.dangosil.cashflow.cashexpense.repository.CashExpenseRepository;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CashExpenseRepositoryIntegrationTest {

    @Autowired
    private CashExpenseRepository cashExpenseRepository;

    @Autowired
    private CashEntryRepository cashEntryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        cashExpenseRepository.deleteAll();
        cashEntryRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldSumOnlyActiveExpensesByDate() {
        Category expenseCategory = categoryRepository.save(new Category("Rent", CategoryType.EXPENSE));
        LocalDate targetDate = LocalDate.of(2026, 6, 8);

        cashExpenseRepository.save(new CashExpense(
                "Store rent",
                new BigDecimal("800.00"),
                targetDate,
                expenseCategory,
                PaymentMethod.BANK_TRANSFER,
                null
        ));
        cashExpenseRepository.save(new CashExpense(
                "Internet",
                new BigDecimal("120.35"),
                targetDate,
                expenseCategory,
                PaymentMethod.PIX,
                null
        ));
        CashExpense inactiveExpense = new CashExpense(
                "Cancelled expense",
                new BigDecimal("999.00"),
                targetDate,
                expenseCategory,
                PaymentMethod.PIX,
                null
        );
        inactiveExpense.deactivate();
        cashExpenseRepository.save(inactiveExpense);
        cashExpenseRepository.save(new CashExpense(
                "Another day expense",
                new BigDecimal("500.00"),
                targetDate.plusDays(1),
                expenseCategory,
                PaymentMethod.PIX,
                null
        ));

        BigDecimal total = cashExpenseRepository.sumActiveAmountByExpenseDate(targetDate);

        assertThat(total).isEqualByComparingTo("920.35");
    }

    @Test
    void shouldSumOnlyActiveExpensesByPeriod() {
        Category expenseCategory = categoryRepository.save(new Category("Supplies", CategoryType.EXPENSE));
        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 30);

        cashExpenseRepository.save(new CashExpense(
                "Start month supplies",
                new BigDecimal("110.00"),
                startDate,
                expenseCategory,
                PaymentMethod.PIX,
                null
        ));
        cashExpenseRepository.save(new CashExpense(
                "End month supplies",
                new BigDecimal("310.00"),
                endDate,
                expenseCategory,
                PaymentMethod.BANK_TRANSFER,
                null
        ));
        CashExpense inactiveExpense = new CashExpense(
                "Inactive month supplies",
                new BigDecimal("700.00"),
                LocalDate.of(2026, 6, 15),
                expenseCategory,
                PaymentMethod.PIX,
                null
        );
        inactiveExpense.deactivate();
        cashExpenseRepository.save(inactiveExpense);
        cashExpenseRepository.save(new CashExpense(
                "Next month supplies",
                new BigDecimal("900.00"),
                LocalDate.of(2026, 7, 1),
                expenseCategory,
                PaymentMethod.PIX,
                null
        ));

        BigDecimal total = cashExpenseRepository.sumActiveAmountByExpenseDateBetween(startDate, endDate);

        assertThat(total).isEqualByComparingTo("420.00");
    }

    @Test
    void shouldFilterExpensesByPeriodCategoryPaymentMethodAndActiveStatus() {
        Category rentCategory = categoryRepository.save(new Category("Rent", CategoryType.EXPENSE));
        Category suppliesCategory = categoryRepository.save(new Category("Supplies", CategoryType.EXPENSE));
        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 30);
        CashExpense expectedExpense = cashExpenseRepository.save(new CashExpense(
                "Expected rent",
                new BigDecimal("150.00"),
                LocalDate.of(2026, 6, 8),
                rentCategory,
                PaymentMethod.PIX,
                null
        ));
        cashExpenseRepository.save(new CashExpense(
                "Wrong category",
                new BigDecimal("200.00"),
                LocalDate.of(2026, 6, 8),
                suppliesCategory,
                PaymentMethod.PIX,
                null
        ));
        cashExpenseRepository.save(new CashExpense(
                "Wrong payment",
                new BigDecimal("300.00"),
                LocalDate.of(2026, 6, 8),
                rentCategory,
                PaymentMethod.CASH,
                null
        ));
        CashExpense inactiveExpense = new CashExpense(
                "Inactive rent",
                new BigDecimal("400.00"),
                LocalDate.of(2026, 6, 8),
                rentCategory,
                PaymentMethod.PIX,
                null
        );
        inactiveExpense.deactivate();
        cashExpenseRepository.save(inactiveExpense);

        List<CashExpense> expenses = cashExpenseRepository.findAllWithFilters(
                startDate,
                endDate,
                rentCategory.getId(),
                PaymentMethod.PIX,
                true
        );

        assertThat(expenses).extracting(CashExpense::getId).containsExactly(expectedExpense.getId());
    }
}
