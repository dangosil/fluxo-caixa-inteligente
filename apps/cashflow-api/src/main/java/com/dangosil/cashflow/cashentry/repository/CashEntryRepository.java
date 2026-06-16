package com.dangosil.cashflow.cashentry.repository;

import com.dangosil.cashflow.cashentry.entity.CashEntry;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CashEntryRepository extends JpaRepository<CashEntry, UUID> {

    @Query("""
            SELECT cashEntry
            FROM CashEntry cashEntry
            WHERE cashEntry.active = :active
              AND cashEntry.entryDate >= COALESCE(:startDate, cashEntry.entryDate)
              AND cashEntry.entryDate <= COALESCE(:endDate, cashEntry.entryDate)
              AND cashEntry.category.id = COALESCE(:categoryId, cashEntry.category.id)
              AND cashEntry.paymentMethod = COALESCE(:paymentMethod, cashEntry.paymentMethod)
            ORDER BY cashEntry.entryDate DESC, cashEntry.createdAt DESC
            """)
    List<CashEntry> findAllWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("categoryId") UUID categoryId,
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("active") boolean active
    );

    @Query("""
            SELECT COALESCE(SUM(
                CASE
                    WHEN cashEntry.feePayer = com.dangosil.cashflow.cashentry.enums.FeePayer.MERCHANT
                        THEN cashEntry.amount - cashEntry.feeAmount
                    ELSE cashEntry.amount
                END
            ), 0)
            FROM CashEntry cashEntry
            WHERE cashEntry.active = true
              AND cashEntry.expectedReceiptDate = :expectedReceiptDate
            """)
    BigDecimal sumActiveAmountByExpectedReceiptDate(
            @Param("expectedReceiptDate") LocalDate expectedReceiptDate
    );

    @Query("""
            SELECT COALESCE(SUM(
                CASE
                    WHEN cashEntry.feePayer = com.dangosil.cashflow.cashentry.enums.FeePayer.MERCHANT
                        THEN cashEntry.amount - cashEntry.feeAmount
                    ELSE cashEntry.amount
                END
            ), 0)
            FROM CashEntry cashEntry
            WHERE cashEntry.active = true
              AND cashEntry.expectedReceiptDate >= :startDate
              AND cashEntry.expectedReceiptDate <= :endDate
            """)
    BigDecimal sumActiveAmountByExpectedReceiptDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
