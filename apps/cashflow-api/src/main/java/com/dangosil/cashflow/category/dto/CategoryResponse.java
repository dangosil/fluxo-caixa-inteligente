package com.dangosil.cashflow.category.dto;

import com.dangosil.cashflow.category.enums.CategoryType;
import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        CategoryType type,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
