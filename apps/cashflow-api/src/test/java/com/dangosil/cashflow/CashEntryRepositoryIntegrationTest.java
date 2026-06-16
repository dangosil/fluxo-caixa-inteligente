package com.dangosil.cashflow;

import static org.assertj.core.api.Assertions.assertThat;

import com.dangosil.cashflow.cashentry.entity.CashEntry;
import com.dangosil.cashflow.cashentry.enums.FeePayer;
import com.dangosil.cashflow.cashentry.repository.CashEntryRepository;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.category.repository.CategoryRepository;
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
class CashEntryRepositoryIntegrationTest {

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
    void shouldSumOnlyActiveEntriesByExpectedReceiptDate() {
        Category incomeCategory = categoryRepository.save(new Category("Sales", CategoryType.INCOME));
        LocalDate targetDate = LocalDate.of(2026, 6, 8);

        cashEntryRepository.save(new CashEntry(
                "Morning sale",
                new BigDecimal("100.00"),
                targetDate,
                incomeCategory,
                PaymentMethod.PIX,
                null,
                targetDate,
                new BigDecimal("10.00"),
                FeePayer.MERCHANT,
                null,
                1,
                null
        ));
        cashEntryRepository.save(new CashEntry(
                "Afternoon sale",
                new BigDecimal("250.50"),
                targetDate,
                incomeCategory,
                PaymentMethod.CASH,
                null,
                targetDate,
                new BigDecimal("5.00"),
                FeePayer.CUSTOMER,
                null,
                1,
                null
        ));
        CashEntry inactiveEntry = new CashEntry(
                "Cancelled sale",
                new BigDecimal("999.00"),
                targetDate,
                incomeCategory,
                PaymentMethod.PIX,
                null
        );
        inactiveEntry.deactivate();
        cashEntryRepository.save(inactiveEntry);
        cashEntryRepository.save(new CashEntry(
                "Another day sale",
                new BigDecimal("500.00"),
                targetDate,
                incomeCategory,
                PaymentMethod.PIX,
                null,
                targetDate.plusDays(1),
                BigDecimal.ZERO,
                FeePayer.MERCHANT,
                null,
                1,
                null
        ));

        BigDecimal total = cashEntryRepository.sumActiveAmountByExpectedReceiptDate(targetDate);

        assertThat(total).isEqualByComparingTo("340.50");
    }

    @Test
    void shouldSumOnlyActiveEntriesByExpectedReceiptDatePeriod() {
        Category incomeCategory = categoryRepository.save(new Category("Services", CategoryType.INCOME));
        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 30);

        cashEntryRepository.save(new CashEntry(
                "Start month service",
                new BigDecimal("100.00"),
                startDate,
                incomeCategory,
                PaymentMethod.PIX,
                null,
                startDate,
                new BigDecimal("10.00"),
                FeePayer.MERCHANT,
                null,
                1,
                null
        ));
        cashEntryRepository.save(new CashEntry(
                "End month service",
                new BigDecimal("300.00"),
                endDate,
                incomeCategory,
                PaymentMethod.BANK_TRANSFER,
                null,
                endDate,
                new BigDecimal("15.00"),
                FeePayer.CUSTOMER,
                null,
                1,
                null
        ));
        CashEntry inactiveEntry = new CashEntry(
                "Inactive month service",
                new BigDecimal("700.00"),
                LocalDate.of(2026, 6, 15),
                incomeCategory,
                PaymentMethod.PIX,
                null
        );
        inactiveEntry.deactivate();
        cashEntryRepository.save(inactiveEntry);
        cashEntryRepository.save(new CashEntry(
                "Next month service",
                new BigDecimal("900.00"),
                LocalDate.of(2026, 6, 15),
                incomeCategory,
                PaymentMethod.PIX,
                null,
                LocalDate.of(2026, 7, 1),
                BigDecimal.ZERO,
                FeePayer.MERCHANT,
                null,
                1,
                null
        ));

        BigDecimal total = cashEntryRepository.sumActiveAmountByExpectedReceiptDateBetween(startDate, endDate);

        assertThat(total).isEqualByComparingTo("390.00");
    }

    @Test
    void shouldFilterEntriesByPeriodCategoryPaymentMethodAndActiveStatus() {
        Category salesCategory = categoryRepository.save(new Category("Sales", CategoryType.INCOME));
        Category servicesCategory = categoryRepository.save(new Category("Services", CategoryType.INCOME));
        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 30);
        CashEntry expectedEntry = cashEntryRepository.save(new CashEntry(
                "Expected sale",
                new BigDecimal("150.00"),
                LocalDate.of(2026, 6, 8),
                salesCategory,
                PaymentMethod.PIX,
                null
        ));
        cashEntryRepository.save(new CashEntry(
                "Wrong category",
                new BigDecimal("200.00"),
                LocalDate.of(2026, 6, 8),
                servicesCategory,
                PaymentMethod.PIX,
                null
        ));
        cashEntryRepository.save(new CashEntry(
                "Wrong payment",
                new BigDecimal("300.00"),
                LocalDate.of(2026, 6, 8),
                salesCategory,
                PaymentMethod.CASH,
                null
        ));
        CashEntry inactiveEntry = new CashEntry(
                "Inactive sale",
                new BigDecimal("400.00"),
                LocalDate.of(2026, 6, 8),
                salesCategory,
                PaymentMethod.PIX,
                null
        );
        inactiveEntry.deactivate();
        cashEntryRepository.save(inactiveEntry);

        List<CashEntry> entries = cashEntryRepository.findAllWithFilters(
                startDate,
                endDate,
                salesCategory.getId(),
                PaymentMethod.PIX,
                true
        );

        assertThat(entries).extracting(CashEntry::getId).containsExactly(expectedEntry.getId());
    }
}
