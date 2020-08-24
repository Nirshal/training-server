package com.nirshal.services.impl;

import com.nirshal.model.Training;
import com.nirshal.model.TrainingFile;
import com.nirshal.model.TrainingInfo;
import com.nirshal.model.mappers.TrainingMapper;
import com.nirshal.repository.MongoDriver;
import com.nirshal.repository.TrainingFileRepository;
import com.nirshal.repository.TrainingRepository;
import com.nirshal.services.TrainingService;
import com.nirshal.services.util.FileContainer;
import com.nirshal.util.decoders.TrainingFileDecoder;
import com.nirshal.util.excel.XLSOutputWriter;
import com.nirshal.util.mongodb.Page;
import lombok.Cleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TrainingServiceImpl implements TrainingService {

    @Inject
    TrainingRepository trainingRepository;
    @Inject
    TrainingFileRepository fileRepository;

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
        // if already existing we save comments etc in order to not overwrite them
        if (trainingRepository.exists(decodedTraining.getId())){
            Training existingTraining = trainingRepository.findById(decodedTraining.getId());
            decodedTraining.setDescription(existingTraining.getDescription());
            decodedTraining.setComments(existingTraining.getComments());
        }
        trainingRepository.upsert(decodedTraining);
        logger.info("Saving source file in the repository.");
        @Cleanup FileInputStream fileData = new FileInputStream(file);
        fileRepository.upsert(
                new TrainingFile(
                        decodedTraining.getId(),
                        fileData.readAllBytes()
                )
        );
        return decodedTraining;
    }

    @Override
    public Training get(String id) {
        logger.info("Fetching training with id={} from repository.", id);
        return trainingRepository.findById(id);
    }

    @Override
    public FileContainer getFile(String id) {
        logger.info("Fetching training file with id={} from repository.", id);
        Training training = get(id);
        TrainingFile trainingFile = fileRepository.findById(id);

        return new FileContainer
                (
                        training.getSport()
                                + "_"
                                + training.getCreationDate()
                                + "_" +
                                training.getId()
                                + ".fit",
                        new ByteArrayInputStream(trainingFile.getFile())
                );
    }

    @Override
    public List<TrainingInfo> get(Page page) {
        logger.info("Fetching trainings list with paging={}", page);
        return trainingRepository
                .query(page)
                .stream()
                .map(trainingMapper::trainingToTrainingInfo)
                .collect(Collectors.toList());
    }
    @Override
    public Training update(Training training) {
        logger.info("Updating training with id={}.", training.getId());
        trainingRepository.upsert(training);
        return training;
    }

    @Override
    public Training delete(String trainingId) {
        logger.info("Deleting training with id={}.", trainingId);
        Training training = trainingRepository.findById(trainingId);
        if (training != null) {
            trainingRepository.deleteById(trainingId);
            fileRepository.deleteById(trainingId);
        }
        return training;
    }

    @Override
    public List<TrainingInfo> getByDates(Date from, Date to) {
        return getTrainingsByDates(from,to)
                .stream()
                .map(trainingMapper::trainingToTrainingInfo)
                .collect(Collectors.toList());
    }

    private List<Training> getTrainingsByDates(Date from, Date to) {
        logger.info("Fetching trainings list from {} to {}", from, to);
        return trainingRepository.getByDates(from,to);
    }

    @Override
    public FileContainer export(Date from, Date to) throws IOException {
        logger.info("Exporting trainings from {} to {}", from, to);
        return new FileContainer
                (
                        "Diary"
                                + ".xls",
                        new ByteArrayInputStream(XLSOutputWriter.export(getTrainingsByDates(from,to)))
                );
    }

    @Override
    public TrainingInfo update(String trainingId, String description, String comments) {

        logger.info("Updating training with id={}.", trainingId);
        Training training = trainingRepository.findById(trainingId);
        if (training != null) {

            if (description != null) training.setDescription(description);
            if (comments != null) training.setComments(comments);
            trainingRepository.upsert(training);

            logger.info("Updated training with id={}.", training.getId());
            return trainingMapper.trainingToTrainingInfo(training);

        } else {
            logger.info("No training with id={} is present in the repository!", trainingId);
            // TODO: exception?
            return null;
        }
    }

}
