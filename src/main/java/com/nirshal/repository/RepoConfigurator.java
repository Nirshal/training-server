package com.nirshal.repository;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class RepoConfigurator {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

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
            logger.info("USING VANILLA MONGO");
            return vanillaTrainingRepository;
        } else {
            logger.info("USING PANACHE MONGO");
            return trainingRepository;
        }
    }

    @Produces
    TrainingFileRepository getTrainigFileRepo(){
        if (vanilla_mongo){
            logger.info("USING VANILLA MONGO");
            return vanillaFileRepository;
        } else {
            logger.info("USING PANACHE MONGO");
            return fileRepository;
        }
    }

}
