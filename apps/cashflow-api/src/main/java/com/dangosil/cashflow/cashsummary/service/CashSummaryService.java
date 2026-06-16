package com.dangosil.cashflow.cashsummary.service;

import com.dangosil.cashflow.cashentry.repository.CashEntryRepository;
import com.dangosil.cashflow.cashexpense.repository.CashExpenseRepository;
import com.dangosil.cashflow.cashsummary.dto.DailyCashSummaryResponse;
import com.dangosil.cashflow.cashsummary.dto.MonthlyCashSummaryResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CashSummaryService {

    private final CashEntryRepository cashEntryRepository;
    private final CashExpenseRepository cashExpenseRepository;

    public CashSummaryService(
            CashEntryRepository cashEntryRepository,
            CashExpenseRepository cashExpenseRepository
    ) {
        this.cashEntryRepository = cashEntryRepository;
        this.cashExpenseRepository = cashExpenseRepository;
    }

    @Transactional(readOnly = true)
    public DailyCashSummaryResponse getDailySummary(LocalDate date) {
        BigDecimal totalIncome = zeroIfNull(cashEntryRepository.sumActiveAmountByExpectedReceiptDate(date));
        BigDecimal totalExpense = zeroIfNull(cashExpenseRepository.sumActiveAmountByExpenseDate(date));
        BigDecimal estimatedProfit = totalIncome.subtract(totalExpense);

        return new DailyCashSummaryResponse(date, totalIncome, totalExpense, estimatedProfit);
    }

    @Transactional(readOnly = true)
    public MonthlyCashSummaryResponse getMonthlySummary(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        BigDecimal totalIncome = zeroIfNull(cashEntryRepository.sumActiveAmountByExpectedReceiptDateBetween(startDate, endDate));
        BigDecimal totalExpense = zeroIfNull(cashExpenseRepository.sumActiveAmountByExpenseDateBetween(startDate, endDate));
        BigDecimal estimatedProfit = totalIncome.subtract(totalExpense);

        return new MonthlyCashSummaryResponse(year, month, totalIncome, totalExpense, estimatedProfit);
    }

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
