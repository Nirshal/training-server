package com.nirshal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Length {
    String type;
    String swimStroke;
    LocalDateTime startTime;
    LocalDateTime timestamp;
    Double totalElapsedTime;
    Double totalTimerTime;
    Double avgSpeed;
    Integer avgSwimmingCadence;
    Integer totalStrokes;
    Integer totalCalories;

}

