package com.nirshal.repository;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class RepoConfigurator {

    @Inject @MongoDriver(MongoDriver.Type.VANILLA)
    TrainingRepository vanillaTrainingRepository;
    @Inject @MongoDriver(MongoDriver.Type.VANILLA)
    TrainingFileRepository vanillaFileRepository;

    @Inject @MongoDriver(MongoDriver.Type.PANACHE)
    TrainingRepository trainingRepository;
    @Inject @MongoDriver(MongoDriver.Type.PANACHE)
    TrainingFileRepository fileRepository;

    @ConfigProperty(name = "vanilla_mongo")
    Boolean vanilla_mongo;

    @Produces
    TrainingRepository getTrainigRepo(){
        if (vanilla_mongo){
            System.out.println("USING VANILLA MONGO");
            return vanillaTrainingRepository;
        } else {
            System.out.println("USING PANACHE MONGO");
            return trainingRepository;
        }
    }

    @Produces
    TrainingFileRepository getTrainigFileRepo(){
        if (vanilla_mongo){
            System.out.println("USING VANILLA MONGO");
            return vanillaFileRepository;
        } else {
            System.out.println("USING PANACHE MONGO");
            return fileRepository;
        }
    }

}
