package com.nirshal.repository.impl.panache;

import com.nirshal.model.TrainingFile;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

@ApplicationScoped
@Default
public class TrainingFileRepo implements PanacheMongoRepositoryBase<TrainingFile, String> {

}
