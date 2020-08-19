package com.nirshal.repository.impl;

import com.nirshal.model.TrainingFile;
import com.nirshal.repository.TrainingFileRepository;
import com.nirshal.util.mongodb.MongoCollections;
import com.nirshal.util.mongodb.MongoRepository;
import lombok.Data;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Data
public class TrainingFileRepositoryImpl implements TrainingFileRepository {

    @Inject
    MongoCollections collections;

    MongoRepository<TrainingFile> repo;

    @PostConstruct
    void init(){
        repo = collections.getRepositoryFrom(TrainingFile.class);
    }

}
