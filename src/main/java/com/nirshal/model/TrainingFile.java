package com.nirshal.model;

import com.nirshal.util.mongodb.MongoItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingFile implements MongoItem {

    private String id;
    private byte[] file;

}
