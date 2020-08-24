package com.nirshal.repository.impl.vanilla;

import com.mongodb.client.result.DeleteResult;
import com.nirshal.model.TrainingFile;
import com.nirshal.repository.MongoDriver;
import com.nirshal.repository.TrainingFileRepository;
import com.nirshal.util.mongodb.MongoCollections;
import com.nirshal.util.mongodb.MongoRepository;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@MongoDriver(MongoDriver.Type.VANILLA)
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
    public Boolean deleteById(String id){
        DeleteResult result = repo.deleteById(id);
        return result.getDeletedCount() > 0L;
    }

}
