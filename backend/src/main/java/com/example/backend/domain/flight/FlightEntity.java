package com.example.backend.domain.flight;

import com.example.backend.domain.drone.RecordTimestampsComparator;
import com.example.backend.domain.flightRecord.FlightRecordEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;

@Entity
public class FlightEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Getter
    @Setter
    private Long Id;
    @Getter
    @Setter
    private LocalDate startDate;
    @Getter
    @Setter
    private LocalTime startTime;
    @Getter
    @Setter
    private LocalDate endDate;
    @Getter
    @Setter
    private LocalTime endTime;
    @Getter
    @Setter
    private LocalTime duration;
    @Getter
    @Setter
    private double averageSpeed;
    @Getter
    @Setter
    private int elevationGain;
    @Getter
    @Setter
    private double distance;
    @Getter
    @Setter
    private boolean didLand;

    @OneToMany
    @Getter
    @Setter
    private List<FlightRecordEntity> flightRecords;

    public void summarizeFlight(List<FlightRecordEntity> flightRecords, Boolean didLanded) {
        this.flightRecords = flightRecords;
        this.didLand = didLanded;

        var sortedFlightRecords = flightRecords.stream().sorted(new RecordTimestampsComparator()).toList();

        var startDate = sortedFlightRecords.get(sortedFlightRecords.size()-1).getDate();
        var startTime = sortedFlightRecords.get(sortedFlightRecords.size()-1).getTime();
        var endDate = sortedFlightRecords.get(0).getDate();
        var endTime = sortedFlightRecords.get(0).getTime();

        setFlightTimeBoundariesWithDuration(startDate, startTime, endDate, endTime);
        setFlightStatistics(flightRecords);
    }

    private void setFlightTimeBoundariesWithDuration(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime ) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.duration = calculateDuration(startDate, startTime, endDate, endTime);
    }

    private LocalTime calculateDuration(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) throws NullPointerException {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        Duration duration = Duration.between(startDateTime, endDateTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return LocalTime.of((int) hours, (int) minutes, (int) seconds);
    }

    private void setFlightStatistics(List<FlightRecordEntity> flightRecords) {
        this.averageSpeed = calculateAverageSpeed(flightRecords);
        this.elevationGain = calculateElevationGain(flightRecords);
        this.distance = calculateDistance(flightRecords);
    }

    private double calculateAverageSpeed(List<FlightRecordEntity> flightRecords) {
        return flightRecords.stream().map(FlightRecordEntity::getSpeed).mapToDouble(d -> d).average().getAsDouble();
    }

    private int calculateElevationGain(List<FlightRecordEntity> flightRecords) {
        return IntStream.range(1, flightRecords.size())
                .map(i -> flightRecords.get(i).getAltitude() - flightRecords.get(i-1).getAltitude())
                .filter(change -> change > 0)
                .sum();
    }

    private double calculateDistance(List<FlightRecordEntity> flightRecords) {
        double distance = 0;

        for(int i = 0; i < flightRecords.size() - 1; i++) {
            distance += calculateDistanceBetweenPoints(
                    flightRecords.get(i).getLatitude(),
                    flightRecords.get(i).getLongitude(),
                    flightRecords.get(i+1).getLatitude(),
                    flightRecords.get(i+1).getLongitude());
        }

        return distance;
    }

    private double calculateDistanceBetweenPoints(double startLatitude,
                                                  double startLongitude,
                                                  double endLatitude,
                                                  double endLongitude) {
        double startLatitudeRadians = Math.toRadians(startLatitude);
        double startLongitudeRadians = Math.toRadians(startLongitude);
        double endLatitudeRadians = Math.toRadians(endLatitude);
        double endLongitudeRadians = Math.toRadians(endLongitude);

        double latitudeDelta = endLatitudeRadians - startLatitudeRadians;
        double longitudeDelta = endLongitudeRadians - startLongitudeRadians;

        //Haversine formula
        double a = Math.sin(latitudeDelta / 2) * Math.sin(latitudeDelta / 2) +
                Math.cos(startLatitudeRadians) * Math.cos(startLongitudeRadians) *
                        Math.sin(longitudeDelta / 2) * Math.sin(longitudeDelta / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        //Earth radius
        return 6371.0 * c;
    }
}
