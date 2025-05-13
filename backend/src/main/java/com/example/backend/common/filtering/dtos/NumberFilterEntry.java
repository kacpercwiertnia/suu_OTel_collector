package com.example.backend.common.filtering.dtos;

import jakarta.validation.constraints.NotNull;

public record NumberFilterEntry(
        @NotNull(message = "filter parameter cannot be null")
        String parameter,
        String key,
        @NotNull(message = "filter value cannot be null")
        double value,
        @NotNull(message = "filter comparison type cannot be null")
        String comparisonType
){}