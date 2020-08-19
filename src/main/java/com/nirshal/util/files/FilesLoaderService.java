package com.nirshal.util.files;

import com.nirshal.model.Training;
import com.nirshal.model.TrainingInfo;
import com.nirshal.model.mappers.TrainingMapper;
import com.nirshal.repository.TrainingRepository;
import com.nirshal.util.decoders.TrainingFileDecoder;
import com.nirshal.util.mongodb.MongoRepositoryCommonQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FilesLoaderService {

    @Inject
    TrainingRepository trainingRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    TrainingFileDecoder decoder;

    @Inject
    TrainingMapper trainingMapper;

    private Training load(String fileName) throws IOException {
        logger.info("Sending file {} to decoder.", fileName);
        File file = new File(fileName);
        Training decodedTraining = decoder.decode(file);
        logger.info("Saving decoded file in the repository.");
        // if already existing we save comments etc in order to not overwrite them
        if (trainingRepository.getRepo().existsAtLeastOne(MongoRepositoryCommonQueries.hasThisId(decodedTraining.getId()))){
            Training existingTraining = trainingRepository.getRepo().findById(decodedTraining.getId());
            decodedTraining.setDescription(existingTraining.getDescription());
            decodedTraining.setComments(existingTraining.getComments());
        }
        // Now we save:
        trainingRepository.getRepo().upsert(decodedTraining);

        return decodedTraining;
    }

    public List<TrainingInfo> loadFiles(String inputFileOrDirectoryName) throws IOException {
        List<TrainingInfo> importedTrainings = new ArrayList<>();
        if (FileManager.isDirectory(inputFileOrDirectoryName)) {
            logger.info("Processing files in {}", inputFileOrDirectoryName);
            // Filter only FIT files.
            List<String> fileNames = FileManager.listFilesWithExtension(inputFileOrDirectoryName, FileType.FIT.getExtension());
            for (String fileName : fileNames) {
                importedTrainings.add(
                        trainingMapper.trainingToTrainingInfo(
                                load(inputFileOrDirectoryName + "/" + fileName)
                        )
                );
            }
            if (fileNames.isEmpty()) logger.error("No suitable files found in {}", inputFileOrDirectoryName);
        }
        else
        {
            if (FileManager.isFile(inputFileOrDirectoryName)) {
                importedTrainings.add(
                        trainingMapper.trainingToTrainingInfo(
                                load(inputFileOrDirectoryName)
                        )
                );
            }
            else
            {
                logger.error("{}: no such file or directory.", inputFileOrDirectoryName);
            }
        }
        return importedTrainings;
    }
}
