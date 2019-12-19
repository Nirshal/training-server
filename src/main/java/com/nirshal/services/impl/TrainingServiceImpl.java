package com.nirshal.services.impl;

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
        logger.info("Sending file to decoder.");
        Training decodedTraining = decoder.decode(file);
        logger.info("Saving decoded file in the repository.");
        trainingRepository.getRepo().upsert(decodedTraining);

        return decodedTraining;
    }

    public Training get(String id) {
        logger.info("Fetching training with id={} from repository.", id);
        return trainingRepository.getRepo().findById(id);

    }

    public List<Training> get(Page page) {
//        logger.info("Fetching training with id={} from repository.", id);
        return trainingRepository.getRepo().query(page).into(new ArrayList<>());

    }


}
