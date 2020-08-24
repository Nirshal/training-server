package com.nirshal.repository.impl.panache;

import com.nirshal.model.Training;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TrainingRepo implements PanacheMongoRepositoryBase<Training, String> {
    // put your custom logic here as instance methods
}
