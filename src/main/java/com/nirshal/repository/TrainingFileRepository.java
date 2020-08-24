package com.nirshal.repository;

import com.nirshal.model.TrainingFile;

public interface TrainingFileRepository {

    TrainingFile findById(String id);

    void upsert(TrainingFile file);

    Boolean deleteById(String id);
}
