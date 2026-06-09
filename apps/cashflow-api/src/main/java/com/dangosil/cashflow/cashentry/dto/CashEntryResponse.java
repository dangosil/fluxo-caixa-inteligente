package com.dangosil.cashflow.cashentry.dto;

import com.dangosil.cashflow.cashentry.enums.CardBrand;
import com.dangosil.cashflow.cashentry.enums.FeePayer;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
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
        BigDecimal feeAmount,
        FeePayer feePayer,
        CardBrand cardBrand,
        int installmentCount,
        BigDecimal installmentAmount,
        BigDecimal customerPaidAmount,
        BigDecimal receivedAmount,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
