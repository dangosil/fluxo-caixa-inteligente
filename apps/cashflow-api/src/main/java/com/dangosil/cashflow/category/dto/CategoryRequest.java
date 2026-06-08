package com.dangosil.cashflow.category.dto;

import com.dangosil.cashflow.category.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank
        @Size(min = 2, max = 80)
        String name,

        @NotNull
        CategoryType type
) {
}
