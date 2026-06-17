package com.dangosil.cashflow.cashentry.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.dangosil.cashflow.cashentry.enums.CardBrand;
import com.dangosil.cashflow.cashentry.enums.FeePayer;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CashEntryTest {

    @Test
    void shouldKeepAmountsUnchangedWhenThereIsNoFee() {
        CashEntry cashEntry = cashEntry(BigDecimal.ZERO, FeePayer.MERCHANT, 1, null);

        assertThat(cashEntry.getFeeAmount()).isEqualByComparingTo("0.00");
        assertThat(cashEntry.getCustomerPaidAmount()).isEqualByComparingTo("100.00");
        assertThat(cashEntry.getReceivedAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void shouldDiscountFeeFromReceivedAmountWhenMerchantPaysFee() {
        CashEntry cashEntry = cashEntry(new BigDecimal("5.00"), FeePayer.MERCHANT, 1, null);

        assertThat(cashEntry.getCustomerPaidAmount()).isEqualByComparingTo("100.00");
        assertThat(cashEntry.getReceivedAmount()).isEqualByComparingTo("95.00");
    }

    @Test
    void shouldAddFeeToCustomerPaidAmountWhenCustomerPaysFee() {
        CashEntry cashEntry = cashEntry(new BigDecimal("5.00"), FeePayer.CUSTOMER, 1, null);

        assertThat(cashEntry.getCustomerPaidAmount()).isEqualByComparingTo("105.00");
        assertThat(cashEntry.getReceivedAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void shouldKeepInstallmentFieldsInformationalForFinancialAmounts() {
        CashEntry cashEntry = cashEntry(new BigDecimal("5.00"), FeePayer.CUSTOMER, 3, new BigDecimal("35.00"));

        assertThat(cashEntry.getInstallmentCount()).isEqualTo(3);
        assertThat(cashEntry.getInstallmentAmount()).isEqualByComparingTo("35.00");
        assertThat(cashEntry.getCustomerPaidAmount()).isEqualByComparingTo("105.00");
        assertThat(cashEntry.getReceivedAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void shouldStoreExpectedReceiptDateIndependentlyFromEntryDate() {
        LocalDate expectedReceiptDate = LocalDate.of(2026, 6, 20);

        CashEntry cashEntry = new CashEntry(
                "Venda no cartao",
                new BigDecimal("100.00"),
                LocalDate.of(2026, 6, 17),
                category(),
                PaymentMethod.CREDIT_CARD,
                null,
                expectedReceiptDate,
                BigDecimal.ZERO,
                FeePayer.MERCHANT,
                CardBrand.VISA,
                1,
                null
        );

        assertThat(cashEntry.getEntryDate()).isEqualTo(LocalDate.of(2026, 6, 17));
        assertThat(cashEntry.getExpectedReceiptDate()).isEqualTo(expectedReceiptDate);
    }

    private CashEntry cashEntry(
            BigDecimal feeAmount,
            FeePayer feePayer,
            int installmentCount,
            BigDecimal installmentAmount
    ) {
        return new CashEntry(
                "Venda no cartao",
                new BigDecimal("100.00"),
                LocalDate.of(2026, 6, 17),
                category(),
                PaymentMethod.CREDIT_CARD,
                null,
                LocalDate.of(2026, 6, 19),
                feeAmount,
                feePayer,
                CardBrand.VISA,
                installmentCount,
                installmentAmount
        );
    }

    private Category category() {
        return new Category("Venda", CategoryType.INCOME);
    }
}
