package com.example.backend.domain.flight.requests.flightSummaries;

import com.example.backend.common.filtering.dtos.BooleanFilterEntry;
import com.example.backend.common.filtering.dtos.DateAndTimeFilterEntry;
import com.example.backend.common.filtering.dtos.NumberFilterEntry;
import com.example.backend.common.filtering.dtos.TimeFilterEntry;
import com.example.backend.common.sorting.dtos.SortEntry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GetFlightsRequest(
        @NotNull
        @Valid
        List<DateAndTimeFilterEntry> dateAndTimeFilters,

        @NotNull
        @Valid
        List<NumberFilterEntry> numberFilters,

        @NotNull
        @Valid
        List<BooleanFilterEntry> booleanFilters,

        @NotNull
        @Valid
        List<TimeFilterEntry> timeFilters,

        @Valid
        SortEntry sort
) {}
