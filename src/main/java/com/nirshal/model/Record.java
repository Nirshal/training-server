package com.nirshal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {

    LocalDateTime timestamp;
    Double distance;

    Double speed;
    Double enhancedSpeed;
    Integer heartRate;
    Integer calories;

    Integer cadence;
    Double stepLength;

    Double positionLat;
    Double positionLong;
    Double altitude;
    Double enhancedAltitude;
    Integer gpsAccuracy;

}
