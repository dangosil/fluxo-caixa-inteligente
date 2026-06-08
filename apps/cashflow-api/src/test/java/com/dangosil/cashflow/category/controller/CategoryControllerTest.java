package com.dangosil.cashflow.category.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dangosil.cashflow.category.dto.CategoryRequest;
import com.dangosil.cashflow.category.dto.CategoryResponse;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.category.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void shouldCreateCategory() throws Exception {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.of(2026, 6, 8, 10, 0);
        CategoryRequest request = new CategoryRequest("Sales", CategoryType.INCOME);
        CategoryResponse response = new CategoryResponse(id, "Sales", CategoryType.INCOME, true, now, now);

        when(categoryService.create(any(CategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/categories/" + id))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Sales"))
                .andExpect(jsonPath("$.type").value("INCOME"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldFindCategories() throws Exception {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.of(2026, 6, 8, 10, 0);
        CategoryResponse response = new CategoryResponse(id, "Rent", CategoryType.EXPENSE, true, now, now);

        when(categoryService.findAll(eq(CategoryType.EXPENSE), eq(true))).thenReturn(List.of(response));

        mockMvc.perform(get("/categories")
                        .param("type", "EXPENSE")
                        .param("active", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].name").value("Rent"))
                .andExpect(jsonPath("$[0].type").value("EXPENSE"))
                .andExpect(jsonPath("$[0].active").value(true));
    }
}
