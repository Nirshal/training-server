package com.nirshal.services.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.nirshal.model.Training;
import com.nirshal.model.TrainingInfo;
import com.nirshal.model.mappers.TrainingMapper;
import com.nirshal.repository.TrainingRepository;
import com.nirshal.services.TrainingService;
import com.nirshal.services.util.FileContainer;
import com.nirshal.util.decoders.TrainingFileDecoder;
import com.nirshal.util.mongodb.Page;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TrainingServiceImpl implements TrainingService {

    @Inject
    TrainingRepository trainingRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    TrainingFileDecoder decoder;

    @Inject
    TrainingMapper trainingMapper;

    @Override
    public Training add(File file) throws IOException {
        logger.info("Sending file {} to decoder.", file.getName());
        Training decodedTraining = decoder.decode(file);
        logger.info("Saving decoded file in the repository.");
        // TODO: de-comment this to re-enable repo save.
        trainingRepository.getRepo().upsert(decodedTraining);

        return decodedTraining;
    }

    @Override
    public Training get(String id) {
        logger.info("Fetching training with id={} from repository.", id);
        return trainingRepository.getRepo().findById(id);
    }

    @Override
    public FileContainer getFile(String id) {

        Training training = get(id);

        return new FileContainer
                (
                        training.getSport()
                                + "_"
                                + training.getCreationDate()
                                + "_" +
                                training.getId()
                                + ".fit",
                        new ByteArrayInputStream(training.getFile())
                );
    }

    @Override
    public List<TrainingInfo> get(Page page) {
        logger.info("Fetching trainings list with paging={}", page);
        return trainingRepository
                .getRepo()
                .query(page)
                .into(new ArrayList<>())
                .stream()
                .map(trainingMapper::trainingToTrainingInfo)
                .collect(Collectors.toList());
    }
    @Override
    public Training update(Training training) {
        logger.info("Updating training with id={}.", training.getId());
        UpdateResult result = trainingRepository.getRepo().upsert(training);
        if (result.getModifiedCount() ==1){
            logger.info("Updated training with id={}, operation result={}.", training.getId(), result.toString());
            return training;
        } else {
            if (result.getUpsertedId()!= null){
                logger.warn("This Training was NOT present in the repository and then it has been created. New training id={}. Operation result={}.", training.getId(), result.toString());
            } else {
                logger.error("Update of Training with id={} was NOT performed. Operation result={}.", training.getId(), result.toString());
                // TODO: add exception?
            }
            return training;
        }
    }

    @Override
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

    @Override
    public List<TrainingInfo> getByDates(Date from, Date to) {
        logger.info("Fetching trainings list from {} to {}", from, to);

        Bson fromDate = from != null ? Filters.gte(Training.CREATION_DATE_FIELD_NAME, from) : null;
        Bson toDate = to != null ? Filters.lte(Training.CREATION_DATE_FIELD_NAME, to) : null;

        Bson sorting = Sorts.ascending(Training.CREATION_DATE_FIELD_NAME);

        if (fromDate == null && toDate == null){
            return trainingRepository
                    .getRepo().getCollection()
                    .find()
                    .sort(sorting).into(new ArrayList<>())
                    .stream()
                    .map(trainingMapper::trainingToTrainingInfo)
                    .collect(Collectors.toList());
        } else {
            Bson query;
            if (fromDate != null && toDate == null){
                query = fromDate;
            } else {
                if (fromDate == null && toDate != null){
                    query = toDate;
                } else {
                    query = Filters.and
                            (
                                    fromDate,
                                    toDate
                            );
                }
            }
            return trainingRepository
                    .getRepo().getCollection()
                    .find(query)
                    .sort(sorting).into(new ArrayList<>())
                    .stream()
                    .map(trainingMapper::trainingToTrainingInfo)
                    .collect(Collectors.toList());
        }
    }
    @Override
    public TrainingInfo update(String trainingId, String description, String comments) {

        logger.info("Updating training with id={}.", trainingId);
        Training training = trainingRepository.getRepo().findById(trainingId);
        if (training != null) {

            if (description != null) training.setDescription(description);
            if (comments != null) training.setComments(comments);
            UpdateResult result = trainingRepository.getRepo().upsert(training);

            logger.info("Updated training with id={}, operation result={}.", training.getId(), result.toString());
            return trainingMapper.trainingToTrainingInfo(training);

        } else {
            logger.info("No training with id={} is present in the repository!", trainingId);
            // TODO: exception?
            return null;
        }
    }

}
