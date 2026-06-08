package com.dangosil.cashflow.cashentry.entity;

import com.dangosil.cashflow.cashentry.enums.PaymentMethod;
import com.dangosil.cashflow.category.entity.Category;
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

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

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
        this.id = UUID.randomUUID();
        this.description = description;
        this.amount = amount;
        this.entryDate = entryDate;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.active = true;
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

    public LocalDate getEntryDate() {
        return entryDate;
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
            String notes
    ) {
        this.description = description;
        this.amount = amount;
        this.entryDate = entryDate;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
    }

    public void deactivate() {
        this.active = false;
    }
}
