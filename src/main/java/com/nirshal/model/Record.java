package com.nirshal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {

    private LocalDateTime timestamp;
    private Double distance;

    private Double speed;
    private Double enhancedSpeed;
    private Integer heartRate;
    private Integer calories;

    private Integer cadence;
    private Double stepLength;

    private Double positionLat;
    private Double positionLong;
    private Double altitude;
    private Double enhancedAltitude;
    private Integer gpsAccuracy;

}
