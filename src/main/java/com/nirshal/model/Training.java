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

    String id;
    LocalDateTime creationDate;

    List<Lap> laps = new ArrayList();
    List<Record> records = new ArrayList();
    List<Set> sets = new ArrayList();

}
