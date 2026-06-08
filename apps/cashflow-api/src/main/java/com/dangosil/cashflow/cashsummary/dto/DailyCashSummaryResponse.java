package com.dangosil.cashflow.cashsummary.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyCashSummaryResponse(
        LocalDate date,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal estimatedProfit
) {
}
