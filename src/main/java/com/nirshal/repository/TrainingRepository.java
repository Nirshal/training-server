package com.nirshal.repository;

import com.nirshal.model.Training;
import com.nirshal.util.mongodb.MongoRepository;

public interface TrainingRepository {

    MongoRepository<Training> getRepo();
}
