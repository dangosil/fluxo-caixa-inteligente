package com.dangosil.cashflow.cashentry.dto;

import com.dangosil.cashflow.cashentry.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CashEntryResponse(
        UUID id,
        String description,
        BigDecimal amount,
        LocalDate entryDate,
        UUID categoryId,
        String categoryName,
        PaymentMethod paymentMethod,
        String notes,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
