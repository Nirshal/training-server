package com.nirshal.repository.impl;

import com.nirshal.model.Training;
import com.nirshal.repository.TrainingRepository;
import com.nirshal.util.mongodb.MongoCollections;
import com.nirshal.util.mongodb.MongoRepository;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Data
public class TrainingRepositoryImpl implements TrainingRepository {

    @Inject
    MongoCollections collections;

    MongoRepository<Training> repo;

    @PostConstruct
    void init(){
        repo = collections.getRepositoryFrom(Training.class);
    }



}
