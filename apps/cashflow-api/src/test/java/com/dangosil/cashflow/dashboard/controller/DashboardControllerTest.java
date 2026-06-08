package com.dangosil.cashflow.dashboard.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dangosil.cashflow.cashsummary.dto.DailyCashSummaryResponse;
import com.dangosil.cashflow.cashsummary.dto.MonthlyCashSummaryResponse;
import com.dangosil.cashflow.dashboard.dto.DashboardSummaryResponse;
import com.dangosil.cashflow.dashboard.service.DashboardService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DashboardService dashboardService;

    @Test
    void shouldReturnDashboardSummaryForProvidedDate() throws Exception {
        LocalDate date = LocalDate.of(2026, 6, 8);
        DashboardSummaryResponse response = dashboardResponse(date);

        when(dashboardService.getSummary(date)).thenReturn(response);

        mockMvc.perform(get("/dashboard/summary").param("date", "2026-06-08"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2026-06-08"))
                .andExpect(jsonPath("$.daily.totalIncome").value(1500.00))
                .andExpect(jsonPath("$.daily.totalExpense").value(1200.00))
                .andExpect(jsonPath("$.daily.estimatedProfit").value(300.00))
                .andExpect(jsonPath("$.monthly.year").value(2026))
                .andExpect(jsonPath("$.monthly.month").value(6))
                .andExpect(jsonPath("$.monthly.totalIncome").value(1500.00))
                .andExpect(jsonPath("$.monthly.totalExpense").value(1200.00))
                .andExpect(jsonPath("$.monthly.estimatedProfit").value(300.00));
    }

    @Test
    void shouldUseCurrentDateWhenDateIsNotProvided() throws Exception {
        LocalDate today = LocalDate.now();
        DashboardSummaryResponse response = dashboardResponse(today);

        when(dashboardService.getSummary(today)).thenReturn(response);

        mockMvc.perform(get("/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(today.toString()));

        verify(dashboardService).getSummary(eq(today));
    }

    private DashboardSummaryResponse dashboardResponse(LocalDate date) {
        DailyCashSummaryResponse daily = new DailyCashSummaryResponse(
                date,
                new BigDecimal("1500.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("300.00")
        );
        MonthlyCashSummaryResponse monthly = new MonthlyCashSummaryResponse(
                date.getYear(),
                date.getMonthValue(),
                new BigDecimal("1500.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("300.00")
        );

        return new DashboardSummaryResponse(date, daily, monthly);
    }
}
