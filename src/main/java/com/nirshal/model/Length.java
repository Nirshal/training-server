package com.nirshal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Length {
    private String type;
    private String swimStroke;
    private LocalDateTime startTime;
    private LocalDateTime timestamp;
    private Double totalElapsedTime;
    private Double totalTimerTime;
    private Double avgSpeed;
    private Integer avgSwimmingCadence;
    private Integer totalStrokes;
    private Integer totalCalories;
}

