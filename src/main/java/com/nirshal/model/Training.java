package com.nirshal.model;

import com.nirshal.util.mongodb.MongoItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Training implements MongoItem {

    public static final String SWIM_STROKE_NONE = "NONE";
    public static final String CREATION_DATE_FIELD_NAME = "creationDate";

    private String id;
    private String sport;
    private LocalDateTime creationDate;
    private LocalDateTime startTime;
    private Double totalTime; // in seconds
    private Double totalDistance; // in meters
    private Integer totalCalories;
    private Double avgSpeed; // [m/s]
    private String description;
    private String comments;

    private Device device;

    private List<Lap> laps = new ArrayList<>();
    private List<Record> records = new ArrayList<>();
    private List<Set> sets = new ArrayList<>();
    private List<Length> lengths = new ArrayList<>();

}
