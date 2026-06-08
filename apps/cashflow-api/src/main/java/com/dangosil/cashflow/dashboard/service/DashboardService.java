package com.dangosil.cashflow.dashboard.service;

import com.dangosil.cashflow.cashsummary.dto.DailyCashSummaryResponse;
import com.dangosil.cashflow.cashsummary.dto.MonthlyCashSummaryResponse;
import com.dangosil.cashflow.cashsummary.service.CashSummaryService;
import com.dangosil.cashflow.dashboard.dto.DashboardSummaryResponse;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final CashSummaryService cashSummaryService;

    public DashboardService(CashSummaryService cashSummaryService) {
        this.cashSummaryService = cashSummaryService;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary(LocalDate date) {
        DailyCashSummaryResponse daily = cashSummaryService.getDailySummary(date);
        MonthlyCashSummaryResponse monthly = cashSummaryService.getMonthlySummary(date.getYear(), date.getMonthValue());

        return new DashboardSummaryResponse(date, daily, monthly);
    }
}
