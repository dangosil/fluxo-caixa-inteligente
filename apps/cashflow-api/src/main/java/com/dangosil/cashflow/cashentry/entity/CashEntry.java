package com.dangosil.cashflow.cashentry.entity;

import com.dangosil.cashflow.cashentry.enums.CardBrand;
import com.dangosil.cashflow.cashentry.enums.FeePayer;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cash_entries")
public class CashEntry {

    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "fee_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal feeAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee_payer", nullable = false, length = 20)
    private FeePayer feePayer;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_brand", length = 30)
    private CardBrand cardBrand;

    @Column(name = "installment_count", nullable = false)
    private int installmentCount;

    @Column(name = "installment_amount", precision = 15, scale = 2)
    private BigDecimal installmentAmount;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "expected_receipt_date", nullable = false)
    private LocalDate expectedReceiptDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected CashEntry() {
    }

    public CashEntry(
            String description,
            BigDecimal amount,
            LocalDate entryDate,
            Category category,
            PaymentMethod paymentMethod,
            String notes
    ) {
        this(
                description,
                amount,
                entryDate,
                category,
                paymentMethod,
                notes,
                entryDate,
                BigDecimal.ZERO,
                FeePayer.MERCHANT,
                null,
                1,
                null
        );
    }

    public CashEntry(
            String description,
            BigDecimal amount,
            LocalDate entryDate,
            Category category,
            PaymentMethod paymentMethod,
            String notes,
            LocalDate expectedReceiptDate,
            BigDecimal feeAmount,
            FeePayer feePayer,
            CardBrand cardBrand,
            int installmentCount,
            BigDecimal installmentAmount
    ) {
        this.id = UUID.randomUUID();
        this.description = description;
        this.amount = amount;
        this.entryDate = entryDate;
        this.expectedReceiptDate = expectedReceiptDate;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.feeAmount = feeAmount;
        this.feePayer = feePayer;
        this.cardBrand = cardBrand;
        this.installmentCount = installmentCount;
        this.installmentAmount = installmentAmount;
        this.active = true;
    }

    public CashEntry(
            String description,
            BigDecimal amount,
            LocalDate entryDate,
            Category category,
            PaymentMethod paymentMethod,
            String notes,
            BigDecimal feeAmount,
            FeePayer feePayer,
            CardBrand cardBrand,
            int installmentCount,
            BigDecimal installmentAmount
    ) {
        this(
                description,
                amount,
                entryDate,
                category,
                paymentMethod,
                notes,
                entryDate,
                feeAmount,
                feePayer,
                cardBrand,
                installmentCount,
                installmentAmount
        );
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public FeePayer getFeePayer() {
        return feePayer;
    }

    public CardBrand getCardBrand() {
        return cardBrand;
    }

    public int getInstallmentCount() {
        return installmentCount;
    }

    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    public BigDecimal getCustomerPaidAmount() {
        if (feePayer == FeePayer.CUSTOMER) {
            return amount.add(feeAmount);
        }
        return amount;
    }

    public BigDecimal getReceivedAmount() {
        if (feePayer == FeePayer.MERCHANT) {
            return amount.subtract(feeAmount);
        }
        return amount;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public LocalDate getExpectedReceiptDate() {
        return expectedReceiptDate;
    }

    public Category getCategory() {
        return category;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void update(
            String description,
            BigDecimal amount,
            LocalDate entryDate,
            Category category,
            PaymentMethod paymentMethod,
            String notes,
            LocalDate expectedReceiptDate,
            BigDecimal feeAmount,
            FeePayer feePayer,
            CardBrand cardBrand,
            int installmentCount,
            BigDecimal installmentAmount
    ) {
        this.description = description;
        this.amount = amount;
        this.entryDate = entryDate;
        this.expectedReceiptDate = expectedReceiptDate;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.feeAmount = feeAmount;
        this.feePayer = feePayer;
        this.cardBrand = cardBrand;
        this.installmentCount = installmentCount;
        this.installmentAmount = installmentAmount;
    }

    public void update(
            String description,
            BigDecimal amount,
            LocalDate entryDate,
            Category category,
            PaymentMethod paymentMethod,
            String notes,
            BigDecimal feeAmount,
            FeePayer feePayer,
            CardBrand cardBrand,
            int installmentCount,
            BigDecimal installmentAmount
    ) {
        update(
                description,
                amount,
                entryDate,
                category,
                paymentMethod,
                notes,
                entryDate,
                feeAmount,
                feePayer,
                cardBrand,
                installmentCount,
                installmentAmount
        );
    }

    public void deactivate() {
        this.active = false;
    }
}
