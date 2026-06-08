package com.dangosil.cashflow.cashsummary.service;

import com.dangosil.cashflow.cashentry.repository.CashEntryRepository;
import com.dangosil.cashflow.cashexpense.repository.CashExpenseRepository;
import com.dangosil.cashflow.cashsummary.dto.DailyCashSummaryResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
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
        BigDecimal totalIncome = zeroIfNull(cashEntryRepository.sumActiveAmountByEntryDate(date));
        BigDecimal totalExpense = zeroIfNull(cashExpenseRepository.sumActiveAmountByExpenseDate(date));
        BigDecimal estimatedProfit = totalIncome.subtract(totalExpense);

        return new DailyCashSummaryResponse(date, totalIncome, totalExpense, estimatedProfit);
    }

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
