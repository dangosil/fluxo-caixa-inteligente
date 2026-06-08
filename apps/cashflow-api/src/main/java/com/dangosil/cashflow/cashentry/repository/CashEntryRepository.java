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
              AND (:startDate IS NULL OR cashEntry.entryDate >= :startDate)
              AND (:endDate IS NULL OR cashEntry.entryDate <= :endDate)
              AND (:categoryId IS NULL OR cashEntry.category.id = :categoryId)
              AND (:paymentMethod IS NULL OR cashEntry.paymentMethod = :paymentMethod)
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
            SELECT COALESCE(SUM(cashEntry.amount), 0)
            FROM CashEntry cashEntry
            WHERE cashEntry.active = true
              AND cashEntry.entryDate = :entryDate
            """)
    BigDecimal sumActiveAmountByEntryDate(@Param("entryDate") LocalDate entryDate);
}
