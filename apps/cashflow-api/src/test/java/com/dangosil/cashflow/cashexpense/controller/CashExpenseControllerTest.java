package com.dangosil.cashflow.cashexpense.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dangosil.cashflow.cashexpense.dto.CashExpenseRequest;
import com.dangosil.cashflow.cashexpense.dto.CashExpenseResponse;
import com.dangosil.cashflow.cashexpense.service.CashExpenseService;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CashExpenseController.class)
class CashExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CashExpenseService cashExpenseService;

    @Test
    void shouldCreateCashExpense() throws Exception {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDate expenseDate = LocalDate.of(2026, 6, 8);
        LocalDateTime now = LocalDateTime.of(2026, 6, 8, 14, 0);
        CashExpenseRequest request = new CashExpenseRequest(
                "Aluguel",
                new BigDecimal("1200.00"),
                expenseDate,
                categoryId,
                PaymentMethod.BANK_TRANSFER,
                "Pagamento mensal"
        );
        CashExpenseResponse response = new CashExpenseResponse(
                id,
                "Aluguel",
                new BigDecimal("1200.00"),
                expenseDate,
                categoryId,
                "Aluguel",
                PaymentMethod.BANK_TRANSFER,
                "Pagamento mensal",
                true,
                now,
                now
        );

        when(cashExpenseService.create(any(CashExpenseRequest.class))).thenReturn(response);

        mockMvc.perform(post("/cash-expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cash-expenses/" + id))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.description").value("Aluguel"))
                .andExpect(jsonPath("$.amount").value(1200.00))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()))
                .andExpect(jsonPath("$.paymentMethod").value("BANK_TRANSFER"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldFindCashExpenses() throws Exception {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDate expenseDate = LocalDate.of(2026, 6, 8);
        LocalDateTime now = LocalDateTime.of(2026, 6, 8, 14, 0);
        CashExpenseResponse response = new CashExpenseResponse(
                id,
                "Aluguel",
                new BigDecimal("1200.00"),
                expenseDate,
                categoryId,
                "Aluguel",
                PaymentMethod.BANK_TRANSFER,
                null,
                true,
                now,
                now
        );

        when(cashExpenseService.findAll(eq(expenseDate), eq(expenseDate), eq(categoryId), eq(PaymentMethod.BANK_TRANSFER), eq(true)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/cash-expenses")
                        .param("startDate", "2026-06-08")
                        .param("endDate", "2026-06-08")
                        .param("categoryId", categoryId.toString())
                        .param("paymentMethod", "BANK_TRANSFER")
                        .param("active", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].description").value("Aluguel"))
                .andExpect(jsonPath("$[0].paymentMethod").value("BANK_TRANSFER"));
    }
}
