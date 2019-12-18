package com.nirshal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Set {
    String type;
    String exercise;
    //    String subExercise; // NOT USED BY GARMIN?
    LocalDateTime startTime;
    LocalDateTime timestamp;
    Double duration;
    Integer repetitions;
    Double weight;
//    String weightDisplayUnit; // NOT USED BY GARMIN?

}
