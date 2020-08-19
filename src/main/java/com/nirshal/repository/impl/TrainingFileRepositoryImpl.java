package com.nirshal.repository.impl;

import com.nirshal.model.Training;
import com.nirshal.model.TrainingFile;
import com.nirshal.repository.TrainingFileRepository;
import com.nirshal.util.mongodb.MongoCollections;
import com.nirshal.util.mongodb.MongoRepository;
import lombok.Data;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TrainingFileRepositoryImpl implements TrainingFileRepository {

    @Inject
    MongoCollections collections;

    MongoRepository<TrainingFile> repo;

    @PostConstruct
    void init(){
        repo = collections.getRepositoryFrom(TrainingFile.class);
    }

    @Override
    public TrainingFile findById(String id){
        return repo.findById(id);
    }

    @Override
    public void upsert(TrainingFile file){
        repo.upsert(file);
    }
    @Override
    public void deleteById(String id){
        repo.deleteById(id);
    }

}
