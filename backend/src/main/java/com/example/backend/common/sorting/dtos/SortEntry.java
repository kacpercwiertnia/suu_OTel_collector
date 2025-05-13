package com.example.backend.common.sorting.dtos;

import jakarta.validation.constraints.NotNull;

public record SortEntry(
        @NotNull(message = "sort parameter cannot be null")
        String parameter,
        String key,
        @NotNull(message = "sort order type cannot be null")
        String orderType
) {
}
