package com.dangosil.cashflow.cashsummary.controller;

import com.dangosil.cashflow.cashsummary.dto.DailyCashSummaryResponse;
import com.dangosil.cashflow.cashsummary.dto.MonthlyCashSummaryResponse;
import com.dangosil.cashflow.cashsummary.service.CashSummaryService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cash-summary")
public class CashSummaryController {

    private final CashSummaryService cashSummaryService;

    public CashSummaryController(CashSummaryService cashSummaryService) {
        this.cashSummaryService = cashSummaryService;
    }

    @GetMapping("/daily")
    public DailyCashSummaryResponse getDailySummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return cashSummaryService.getDailySummary(date);
    }

    @GetMapping("/monthly")
    public MonthlyCashSummaryResponse getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return cashSummaryService.getMonthlySummary(year, month);
    }
}
