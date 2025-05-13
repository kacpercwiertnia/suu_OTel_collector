package com.example.backend.domain.flight.requests;

import com.example.backend.domain.flight.FlightEntity;
import com.example.backend.domain.flight.FlightService;
import com.example.backend.domain.flight.dto.FlightDto;
import com.example.backend.domain.flight.dto.FlightSummaryDto;
import com.example.backend.domain.flight.filtering.IFlightFilter;
import com.example.backend.domain.flight.requests.flightSummaries.GetFlightsRequest;
import com.example.backend.domain.flight.requests.mappers.FlightFiltersMapper;
import com.example.backend.domain.flight.requests.mappers.FlightSortMapper;
import com.example.backend.domain.flight.sorting.IFlightSort;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDto> getFlight(@PathVariable Long id) {
        Optional<FlightEntity> flight = flightService.getFlight(id);

        if (flight.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        FlightDto dto = FlightDto.fromFlightEntity(flight.get());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/{droneRegistrationNumber}")
    public ResponseEntity<List<FlightSummaryDto>> getFlights(@PathVariable String droneRegistrationNumber,
                                                             @Valid @RequestBody GetFlightsRequest request) throws BadRequestException {
        List<IFlightFilter> mappedFilters;

        try {
            mappedFilters = FlightFiltersMapper.map(request.dateAndTimeFilters(), request.numberFilters(), request.booleanFilters(), request.timeFilters());
        } catch(IllegalArgumentException ex){
            throw new BadRequestException(ex.getMessage());
        }

        Optional<IFlightSort> mappedSort = FlightSortMapper.map(request.sort());

        List<FlightEntity> flights = flightService.getDroneFinishedFlights(droneRegistrationNumber, mappedFilters, mappedSort);

        List<FlightSummaryDto> flightSummaryDtos = flights.stream().map(FlightSummaryDto::fromFlightEntity).toList();

        return new ResponseEntity<>(flightSummaryDtos, HttpStatus.OK);
    }
}
