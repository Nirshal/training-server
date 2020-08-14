package com.nirshal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TrainingInfo {

    private String id;
    private String sport;
    private LocalDateTime creationDate;
    private Integer totalTime; // in seconds
    private Integer totalDistance; // in meters
    private Integer totalCalories;
    private Double avgSpeed; // [m/s]

}
