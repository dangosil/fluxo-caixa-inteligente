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
              AND cashExpense.expenseDate >= COALESCE(:startDate, cashExpense.expenseDate)
              AND cashExpense.expenseDate <= COALESCE(:endDate, cashExpense.expenseDate)
              AND cashExpense.category.id = COALESCE(:categoryId, cashExpense.category.id)
              AND cashExpense.paymentMethod = COALESCE(:paymentMethod, cashExpense.paymentMethod)
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

    @Query("""
            SELECT COALESCE(SUM(cashExpense.amount), 0)
            FROM CashExpense cashExpense
            WHERE cashExpense.active = true
              AND cashExpense.expenseDate >= :startDate
              AND cashExpense.expenseDate <= :endDate
            """)
    BigDecimal sumActiveAmountByExpenseDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
