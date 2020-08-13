package com.nirshal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Set {
    private String type;
    private String exercise;
    //    String subExercise; // NOT USED BY GARMIN?
    private LocalDateTime startTime;
    private LocalDateTime timestamp;
    private Double duration;
    private Integer repetitions;
    private Double weight;
//    String weightDisplayUnit; // NOT USED BY GARMIN?

}
