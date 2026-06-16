package com.dangosil.cashflow.cashentry.dto;

import com.dangosil.cashflow.cashentry.enums.CardBrand;
import com.dangosil.cashflow.cashentry.enums.FeePayer;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CashEntryRequest(
        @NotBlank
        @Size(min = 2, max = 120)
        String description,

        @NotNull
        @DecimalMin(value = "0.00", inclusive = false)
        BigDecimal amount,

        @NotNull
        LocalDate entryDate,

        LocalDate expectedReceiptDate,

        @NotNull
        UUID categoryId,

        @NotNull
        PaymentMethod paymentMethod,

        @Size(max = 500)
        String notes,

        @DecimalMin(value = "0.00")
        BigDecimal feeAmount,

        FeePayer feePayer,

        CardBrand cardBrand,

        @Min(1)
        Integer installmentCount,

        @DecimalMin(value = "0.00")
        BigDecimal installmentAmount
) {
}
