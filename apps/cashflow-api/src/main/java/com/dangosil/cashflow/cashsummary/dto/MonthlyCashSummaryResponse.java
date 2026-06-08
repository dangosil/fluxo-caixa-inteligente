package com.dangosil.cashflow.cashsummary.dto;

import java.math.BigDecimal;

public record MonthlyCashSummaryResponse(
        int year,
        int month,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal estimatedProfit
) {
}
