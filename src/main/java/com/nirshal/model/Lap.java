package com.nirshal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lap {

    private LocalDateTime timestamp;
    private LocalDateTime startTimestamp;

    private String sport;
    private Double distance;

    private Double totalMovingTime;
    private Double totalTimerTime;
    private Double totalElapsedTime;

    private Integer avgCadence;
    private Integer maxCadence;

    private Integer avgHR;
    private Integer minHR;
    private Integer maxHR;
    private Integer totalCalories;

    private Integer avgRunningCadence;
    private Integer maxRunningCadence;

    private Double avgSpeed;
    private Double maxSpeed;

    private Double avgStepLength;

    private Double startPositionLat;
    private Double endPositionLat;

    private Double startPositionLong;
    private Double endPositionLong;
    private Integer gpsAccuracy;

}
