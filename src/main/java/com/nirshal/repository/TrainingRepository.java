package com.nirshal.repository;

import com.nirshal.model.Training;
import com.nirshal.util.mongodb.Page;

import java.util.Date;
import java.util.List;

public interface TrainingRepository {

    Boolean exists(String id);

    Training findById(String id);

    void upsert(Training training);

    List<Training> query(Page page);

    Boolean deleteById(String id);

    List<Training> getByDates(Date from, Date to);
}
