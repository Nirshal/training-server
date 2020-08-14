package com.nirshal.services.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.nirshal.model.Training;
import com.nirshal.repository.TrainingRepository;
import com.nirshal.services.TrainingService;
import com.nirshal.util.decoders.TrainingFileDecoder;
import com.nirshal.util.mongodb.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TrainingServiceImpl implements TrainingService {

    @Inject
    TrainingRepository trainingRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    TrainingFileDecoder decoder;

    public Training add(File file) throws IOException {
        logger.info("Sending file {} to decoder.", file.getName());
        Training decodedTraining = decoder.decode(file);
        logger.info("Saving decoded file in the repository.");
        // TODO: de-comment this to re-enable repo save.
                trainingRepository.getRepo().upsert(decodedTraining);

        return decodedTraining;
    }

    public Training get(String id) {
        logger.info("Fetching training with id={} from repository.", id);
        return trainingRepository.getRepo().findById(id);
    }

    public List<Training> get(Page page) {
        logger.info("Fetching trainings list with paging={}", page);
        return trainingRepository.getRepo().query(page).into(new ArrayList<>());
    }
    public Training update(Training training) {
        logger.info("Updating training with id={}.", training.getId());
        UpdateResult result = trainingRepository.getRepo().upsert(training);
        if (result.isModifiedCountAvailable() && result.getModifiedCount() ==1){
            logger.info("Updated training with id={}, operation result={}.", training.getId(), result.toString());
            return training;
        } else {
            if (result.getUpsertedId()!= null){
                logger.warn("This Training was NOT present in the repository and then it has been created. New training id={}. Operation result={}.", training.getId(), result.toString());
                return training;

            } else {
                logger.error("Update of Training with id={} was NOT performed. Operation result={}.", training.getId(), result.toString());
                // TODO: add exception?
                return training;
            }
        }
    }

    public Training delete(String trainingId) {
        logger.info("Deleting training with id={}.", trainingId);
        Training training = trainingRepository.getRepo().findById(trainingId);
        if (training != null) {
            DeleteResult result = trainingRepository.getRepo().deleteById(trainingId);
            if (result.getDeletedCount() == 1) {
                logger.info("Deleted training with id={}. Result={}", trainingId, result.toString());
                return training;
            } else {
                logger.error("An error occurred while deleting training with id={}. Result={}", trainingId, result.toString());
                // TODO: exception?
                return null;
            }
        } else {
            logger.info("No training with id={} is present in the repository!", trainingId);
            // TODO: exception?
            return null;
        }
    }



}
