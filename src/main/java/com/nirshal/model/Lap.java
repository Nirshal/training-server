package com.nirshal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lap {

    LocalDateTime timestamp;
    LocalDateTime startTimestamp;

    String sport;
    Double distance;

    Double totalMovingTime;
    Double totalTimerTime;
    Double totalElapsedTime;

    Integer avgCadence;
    Integer maxCadence;

    Integer avgHR;
    Integer minHR;
    Integer maxHR;
    Integer totalCalories;

    Integer avgRunningCadence;
    Integer maxRunningCadence;

    Double avgSpeed;
    Double maxSpeed;

    Double avgStepLength;

    Double startPositionLat;
    Double endPositionLat;

    Double startPositionLong;
    Double endPositionLong;
    Integer gpsAccuracy;

}
