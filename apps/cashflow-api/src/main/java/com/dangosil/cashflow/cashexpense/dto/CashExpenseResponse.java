package com.dangosil.cashflow.cashexpense.dto;

import com.dangosil.cashflow.shared.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CashExpenseResponse(
        UUID id,
        String description,
        BigDecimal amount,
        LocalDate expenseDate,
        UUID categoryId,
        String categoryName,
        PaymentMethod paymentMethod,
        String notes,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
