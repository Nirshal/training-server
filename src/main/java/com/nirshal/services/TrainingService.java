package com.nirshal.services;

import com.nirshal.model.Training;
import com.nirshal.model.TrainingInfo;
import com.nirshal.services.util.FileContainer;
import com.nirshal.util.mongodb.Page;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface TrainingService {
    Training add(File file) throws IOException;
    Training get(String id);
    FileContainer getFile(String id) throws IOException;
    List<TrainingInfo> get(Page page);
    Training update(Training training);
    Training delete(String trainingId);
    List<TrainingInfo> getByDates(Date from, Date to);

    TrainingInfo update(String trainingId, String description, String comments);
}
