package com.dangosil.cashflow.cashexpense.repository;

import com.dangosil.cashflow.cashexpense.entity.CashExpense;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CashExpenseRepository extends JpaRepository<CashExpense, UUID> {

    @Query("""
            SELECT cashExpense
            FROM CashExpense cashExpense
            WHERE cashExpense.active = :active
              AND (:startDate IS NULL OR cashExpense.expenseDate >= :startDate)
              AND (:endDate IS NULL OR cashExpense.expenseDate <= :endDate)
              AND (:categoryId IS NULL OR cashExpense.category.id = :categoryId)
              AND (:paymentMethod IS NULL OR cashExpense.paymentMethod = :paymentMethod)
            ORDER BY cashExpense.expenseDate DESC, cashExpense.createdAt DESC
            """)
    List<CashExpense> findAllWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("categoryId") UUID categoryId,
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("active") boolean active
    );

    @Query("""
            SELECT COALESCE(SUM(cashExpense.amount), 0)
            FROM CashExpense cashExpense
            WHERE cashExpense.active = true
              AND cashExpense.expenseDate = :expenseDate
            """)
    BigDecimal sumActiveAmountByExpenseDate(@Param("expenseDate") LocalDate expenseDate);
}
