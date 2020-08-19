package com.nirshal.repository;

import com.nirshal.model.TrainingFile;
import com.nirshal.util.mongodb.MongoRepository;

public interface TrainingFileRepository {

//    MongoRepository<TrainingFile> getRepo();

    TrainingFile findById(String id);

    void upsert(TrainingFile file);

    void deleteById(String id);
}
