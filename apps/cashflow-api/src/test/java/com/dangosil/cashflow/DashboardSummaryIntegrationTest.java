package com.dangosil.cashflow;

import static org.assertj.core.api.Assertions.assertThat;

import com.dangosil.cashflow.cashentry.entity.CashEntry;
import com.dangosil.cashflow.cashentry.enums.FeePayer;
import com.dangosil.cashflow.cashentry.repository.CashEntryRepository;
import com.dangosil.cashflow.cashexpense.entity.CashExpense;
import com.dangosil.cashflow.cashexpense.repository.CashExpenseRepository;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.category.repository.CategoryRepository;
import com.dangosil.cashflow.dashboard.dto.DashboardSummaryResponse;
import com.dangosil.cashflow.dashboard.service.DashboardService;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DashboardSummaryIntegrationTest {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private CashEntryRepository cashEntryRepository;

    @Autowired
    private CashExpenseRepository cashExpenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        cashExpenseRepository.deleteAll();
        cashEntryRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldUseReceivedAmountInDashboardSummary() {
        LocalDate date = LocalDate.of(2026, 6, 8);
        Category incomeCategory = categoryRepository.save(new Category("Sales", CategoryType.INCOME));
        Category expenseCategory = categoryRepository.save(new Category("Supplies", CategoryType.EXPENSE));

        cashEntryRepository.save(new CashEntry(
                "Card sale",
                new BigDecimal("100.00"),
                date,
                incomeCategory,
                PaymentMethod.CREDIT_CARD,
                null,
                new BigDecimal("10.00"),
                FeePayer.MERCHANT,
                null,
                1,
                null
        ));
        cashExpenseRepository.save(new CashExpense(
                "Supplies",
                new BigDecimal("40.00"),
                date,
                expenseCategory,
                PaymentMethod.PIX,
                null
        ));

        DashboardSummaryResponse response = dashboardService.getSummary(date);

        assertThat(response.daily().totalIncome()).isEqualByComparingTo("90.00");
        assertThat(response.daily().totalExpense()).isEqualByComparingTo("40.00");
        assertThat(response.daily().estimatedProfit()).isEqualByComparingTo("50.00");
        assertThat(response.monthly().totalIncome()).isEqualByComparingTo("90.00");
        assertThat(response.monthly().totalExpense()).isEqualByComparingTo("40.00");
        assertThat(response.monthly().estimatedProfit()).isEqualByComparingTo("50.00");
    }
}
