package com.nirshal.util.files;

import com.nirshal.model.TrainingInfo;
import com.nirshal.model.mappers.TrainingMapper;
import com.nirshal.services.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class FilesLoaderService {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    TrainingService trainingService;

    @Inject
    TrainingMapper trainingMapper;

    public List<TrainingInfo> loadFiles(String inputFileOrDirectoryName) throws IOException {
        List<String> fileNames = new ArrayList<>();
        if (FileManager.isDirectory(inputFileOrDirectoryName)) {
            logger.info("Processing files in {}", inputFileOrDirectoryName);
            // Filter only FIT files.
            fileNames =
                    FileManager.listFilesWithExtension(inputFileOrDirectoryName, FileType.FIT.getExtension())
                            .stream()
                            .map(filename -> inputFileOrDirectoryName + "/" + filename)
                            .collect(Collectors.toList());
        } else {
            if (FileManager.isFile(inputFileOrDirectoryName)) {
                fileNames.add(inputFileOrDirectoryName);
            }
            else {
                logger.error("{}: no such file or directory.", inputFileOrDirectoryName);
            }
        }
        return fileNames
                .stream()
                .map(File::new)
                .map( file -> {
                            try {
                                return trainingService.add(file);
                            } catch (IOException e) {
                                logger.error("Error decoding file: {}. Exception={}", file.getName(), e.getMessage());
                                return null;
                            }
                        }
                )
                .filter(Objects::nonNull)
                .map(trainingMapper::trainingToTrainingInfo)
                .collect(Collectors.toList());
    }
}
