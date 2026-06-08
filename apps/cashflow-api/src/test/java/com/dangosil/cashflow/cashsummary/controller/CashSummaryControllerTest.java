package com.dangosil.cashflow.cashsummary.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dangosil.cashflow.cashsummary.dto.DailyCashSummaryResponse;
import com.dangosil.cashflow.cashsummary.dto.MonthlyCashSummaryResponse;
import com.dangosil.cashflow.cashsummary.service.CashSummaryService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CashSummaryController.class)
class CashSummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CashSummaryService cashSummaryService;

    @Test
    void shouldReturnDailyCashSummary() throws Exception {
        LocalDate date = LocalDate.of(2026, 6, 8);
        DailyCashSummaryResponse response = new DailyCashSummaryResponse(
                date,
                new BigDecimal("1500.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("300.00")
        );

        when(cashSummaryService.getDailySummary(date)).thenReturn(response);

        mockMvc.perform(get("/cash-summary/daily").param("date", "2026-06-08"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2026-06-08"))
                .andExpect(jsonPath("$.totalIncome").value(1500.00))
                .andExpect(jsonPath("$.totalExpense").value(1200.00))
                .andExpect(jsonPath("$.estimatedProfit").value(300.00));
    }

    @Test
    void shouldReturnMonthlyCashSummary() throws Exception {
        MonthlyCashSummaryResponse response = new MonthlyCashSummaryResponse(
                2026,
                6,
                new BigDecimal("1500.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("300.00")
        );

        when(cashSummaryService.getMonthlySummary(2026, 6)).thenReturn(response);

        mockMvc.perform(get("/cash-summary/monthly")
                        .param("year", "2026")
                        .param("month", "6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(2026))
                .andExpect(jsonPath("$.month").value(6))
                .andExpect(jsonPath("$.totalIncome").value(1500.00))
                .andExpect(jsonPath("$.totalExpense").value(1200.00))
                .andExpect(jsonPath("$.estimatedProfit").value(300.00));
    }
}
