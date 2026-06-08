package com.dangosil.cashflow.dashboard.dto;

import com.dangosil.cashflow.cashsummary.dto.DailyCashSummaryResponse;
import com.dangosil.cashflow.cashsummary.dto.MonthlyCashSummaryResponse;
import java.time.LocalDate;

public record DashboardSummaryResponse(
        LocalDate date,
        DailyCashSummaryResponse daily,
        MonthlyCashSummaryResponse monthly
) {
}
