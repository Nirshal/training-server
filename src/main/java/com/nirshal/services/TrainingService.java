package com.nirshal.services;

import com.nirshal.model.Training;
import com.nirshal.util.mongodb.Page;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface TrainingService {
    Training add(File file) throws IOException;
    Training get(String id);
    List<Training> get(Page page);
    Training update(Training training);
    Training delete(String trainingId);

}
