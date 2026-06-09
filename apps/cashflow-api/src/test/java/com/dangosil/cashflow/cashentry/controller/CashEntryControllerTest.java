package com.dangosil.cashflow.cashentry.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dangosil.cashflow.cashentry.dto.CashEntryRequest;
import com.dangosil.cashflow.cashentry.dto.CashEntryResponse;
import com.dangosil.cashflow.cashentry.enums.CardBrand;
import com.dangosil.cashflow.cashentry.enums.FeePayer;
import com.dangosil.cashflow.cashentry.service.CashEntryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
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

@WebMvcTest(CashEntryController.class)
class CashEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CashEntryService cashEntryService;

    @Test
    void shouldCreateCashEntry() throws Exception {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDate entryDate = LocalDate.of(2026, 6, 8);
        LocalDateTime now = LocalDateTime.of(2026, 6, 8, 14, 0);
        CashEntryRequest request = new CashEntryRequest(
                "Venda de produto",
                new BigDecimal("1500.00"),
                entryDate,
                categoryId,
                PaymentMethod.PIX,
                "Recebimento registrado no caixa",
                new BigDecimal("30.00"),
                FeePayer.MERCHANT,
                CardBrand.VISA,
                2,
                new BigDecimal("750.00")
        );
        CashEntryResponse response = new CashEntryResponse(
                id,
                "Venda de produto",
                new BigDecimal("1500.00"),
                entryDate,
                categoryId,
                "Venda de produto",
                PaymentMethod.PIX,
                "Recebimento registrado no caixa",
                new BigDecimal("30.00"),
                FeePayer.MERCHANT,
                CardBrand.VISA,
                2,
                new BigDecimal("750.00"),
                new BigDecimal("1500.00"),
                new BigDecimal("1470.00"),
                true,
                now,
                now
        );

        when(cashEntryService.create(any(CashEntryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/cash-entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cash-entries/" + id))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.description").value("Venda de produto"))
                .andExpect(jsonPath("$.amount").value(1500.00))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()))
                .andExpect(jsonPath("$.paymentMethod").value("PIX"))
                .andExpect(jsonPath("$.feeAmount").value(30.00))
                .andExpect(jsonPath("$.feePayer").value("MERCHANT"))
                .andExpect(jsonPath("$.cardBrand").value("VISA"))
                .andExpect(jsonPath("$.installmentCount").value(2))
                .andExpect(jsonPath("$.installmentAmount").value(750.00))
                .andExpect(jsonPath("$.customerPaidAmount").value(1500.00))
                .andExpect(jsonPath("$.receivedAmount").value(1470.00))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldFindCashEntries() throws Exception {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDate entryDate = LocalDate.of(2026, 6, 8);
        LocalDateTime now = LocalDateTime.of(2026, 6, 8, 14, 0);
        CashEntryResponse response = new CashEntryResponse(
                id,
                "Venda de produto",
                new BigDecimal("1500.00"),
                entryDate,
                categoryId,
                "Venda de produto",
                PaymentMethod.PIX,
                null,
                BigDecimal.ZERO,
                FeePayer.MERCHANT,
                null,
                1,
                null,
                new BigDecimal("1500.00"),
                new BigDecimal("1500.00"),
                true,
                now,
                now
        );

        when(cashEntryService.findAll(eq(entryDate), eq(entryDate), eq(categoryId), eq(PaymentMethod.PIX), eq(true)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/cash-entries")
                        .param("startDate", "2026-06-08")
                        .param("endDate", "2026-06-08")
                        .param("categoryId", categoryId.toString())
                        .param("paymentMethod", "PIX")
                        .param("active", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].description").value("Venda de produto"))
                .andExpect(jsonPath("$[0].paymentMethod").value("PIX"));
    }

    @Test
    void shouldFindCashEntryById() throws Exception {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDate entryDate = LocalDate.of(2026, 6, 8);
        LocalDateTime now = LocalDateTime.of(2026, 6, 8, 14, 0);
        CashEntryResponse response = new CashEntryResponse(
                id,
                "Venda de produto",
                new BigDecimal("1500.00"),
                entryDate,
                categoryId,
                "Venda de produto",
                PaymentMethod.PIX,
                null,
                BigDecimal.ZERO,
                FeePayer.MERCHANT,
                null,
                1,
                null,
                new BigDecimal("1500.00"),
                new BigDecimal("1500.00"),
                true,
                now,
                now
        );

        when(cashEntryService.findById(id)).thenReturn(response);

        mockMvc.perform(get("/cash-entries/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.description").value("Venda de produto"));
    }

    @Test
    void shouldReturnValidationErrorWhenRequestIsInvalid() throws Exception {
        CashEntryRequest request = new CashEntryRequest(
                "",
                BigDecimal.ZERO,
                null,
                null,
                null,
                "valid note",
                new BigDecimal("-1.00"),
                null,
                null,
                0,
                new BigDecimal("-1.00")
        );

        mockMvc.perform(post("/cash-entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details").isArray());
    }
}
