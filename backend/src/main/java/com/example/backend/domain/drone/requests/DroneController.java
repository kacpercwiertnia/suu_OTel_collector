package com.example.backend.domain.drone.requests;

import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.domain.drone.DroneService;
import com.example.backend.domain.drone.dto.DroneDto;
import com.example.backend.domain.drone.dto.FlyingDroneDto;
import com.example.backend.domain.drone.dto.FlyingDronesWithTimestampDto;
import com.example.backend.domain.drone.filtering.IDroneFilter;
import com.example.backend.domain.drone.requests.currentlyFlyingDrones.GetCurrentlyFlyingDronesRequest;
import com.example.backend.domain.drone.requests.drones.GetDronesRequest;
import com.example.backend.domain.drone.requests.mappers.DroneFiltersMapper;
import com.example.backend.domain.drone.requests.mappers.DroneSortMapper;
import com.example.backend.domain.drone.sorting.IDroneSort;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/drones")
public class DroneController {
    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping("/")
    public ResponseEntity<List<DroneDto>> getDrones(@Valid @RequestBody GetDronesRequest request) throws BadRequestException {
        List<IDroneFilter> mappedFilters;

        try {
            mappedFilters = DroneFiltersMapper.map(request.textFilters(), request.numberFilters());
        } catch(IllegalArgumentException ex){
            throw new BadRequestException(ex.getMessage());
        }

        Optional<IDroneSort> mappedSort = DroneSortMapper.map(request.sort());

        var drones = droneService.getDrones(mappedFilters, mappedSort);

        var dtos = drones.stream().map(DroneDto::fromDroneEntity).toList();

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/currentlyFlying")
    public ResponseEntity<FlyingDronesWithTimestampDto> getCurrentlyFlyingDrones(@Valid @RequestBody GetCurrentlyFlyingDronesRequest request) throws BadRequestException{
        List<IDroneFilter> mappedFilters;

        try{
            mappedFilters = DroneFiltersMapper.map(request.textFilters(), request.numberFilters());
        }catch(IllegalArgumentException ex){
            throw new BadRequestException(ex.getMessage());
        }

        Optional<IDroneSort> mappedSort = DroneSortMapper.map(request.sort());

        var flyingDrones = droneService.getCurrentlyFlyingDrones(mappedFilters, mappedSort);

        var flyingDroneDtos = flyingDrones.stream().map(FlyingDroneDto::fromDroneEntity).toList();
        var date = LocalDate.now();
        var time = LocalTime.now().withNano(0);

        var dto = new FlyingDronesWithTimestampDto(flyingDroneDtos, date, time);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/currentlyFlying/{registration}")
    public ResponseEntity<FlyingDroneDto> getDroneWithTrace(@PathVariable String registration) {
        Optional<DroneEntity> drone = droneService.getDroneWithCurrentFlightTrace(registration);

        if (drone.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        FlyingDroneDto dto = FlyingDroneDto.fromDroneEntity(drone.get());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
