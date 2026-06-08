package com.dangosil.cashflow.cashsummary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.dangosil.cashflow.cashentry.repository.CashEntryRepository;
import com.dangosil.cashflow.cashexpense.repository.CashExpenseRepository;
import com.dangosil.cashflow.cashsummary.dto.DailyCashSummaryResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CashSummaryServiceTest {

    @Mock
    private CashEntryRepository cashEntryRepository;

    @Mock
    private CashExpenseRepository cashExpenseRepository;

    @InjectMocks
    private CashSummaryService cashSummaryService;

    @Test
    void shouldCalculateDailyEstimatedProfit() {
        LocalDate date = LocalDate.of(2026, 6, 8);

        when(cashEntryRepository.sumActiveAmountByEntryDate(date)).thenReturn(new BigDecimal("1500.00"));
        when(cashExpenseRepository.sumActiveAmountByExpenseDate(date)).thenReturn(new BigDecimal("1200.00"));

        DailyCashSummaryResponse response = cashSummaryService.getDailySummary(date);

        assertThat(response.date()).isEqualTo(date);
        assertThat(response.totalIncome()).isEqualByComparingTo("1500.00");
        assertThat(response.totalExpense()).isEqualByComparingTo("1200.00");
        assertThat(response.estimatedProfit()).isEqualByComparingTo("300.00");
    }

    @Test
    void shouldReturnZeroWhenThereAreNoEntriesOrExpenses() {
        LocalDate date = LocalDate.of(2026, 6, 8);

        when(cashEntryRepository.sumActiveAmountByEntryDate(date)).thenReturn(null);
        when(cashExpenseRepository.sumActiveAmountByExpenseDate(date)).thenReturn(null);

        DailyCashSummaryResponse response = cashSummaryService.getDailySummary(date);

        assertThat(response.date()).isEqualTo(date);
        assertThat(response.totalIncome()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.totalExpense()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.estimatedProfit()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
