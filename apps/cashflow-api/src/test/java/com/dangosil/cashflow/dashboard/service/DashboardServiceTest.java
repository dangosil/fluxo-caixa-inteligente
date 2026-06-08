package com.dangosil.cashflow.dashboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dangosil.cashflow.cashsummary.dto.DailyCashSummaryResponse;
import com.dangosil.cashflow.cashsummary.dto.MonthlyCashSummaryResponse;
import com.dangosil.cashflow.cashsummary.service.CashSummaryService;
import com.dangosil.cashflow.dashboard.dto.DashboardSummaryResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private CashSummaryService cashSummaryService;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void shouldCombineDailyAndMonthlySummaries() {
        LocalDate date = LocalDate.of(2026, 6, 8);
        DailyCashSummaryResponse daily = new DailyCashSummaryResponse(
                date,
                new BigDecimal("1500.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("300.00")
        );
        MonthlyCashSummaryResponse monthly = new MonthlyCashSummaryResponse(
                2026,
                6,
                new BigDecimal("1500.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("300.00")
        );

        when(cashSummaryService.getDailySummary(date)).thenReturn(daily);
        when(cashSummaryService.getMonthlySummary(2026, 6)).thenReturn(monthly);

        DashboardSummaryResponse response = dashboardService.getSummary(date);

        assertThat(response.date()).isEqualTo(date);
        assertThat(response.daily()).isEqualTo(daily);
        assertThat(response.monthly()).isEqualTo(monthly);
        verify(cashSummaryService).getDailySummary(date);
        verify(cashSummaryService).getMonthlySummary(2026, 6);
    }
}
