package com.nirshal.rest;

import com.nirshal.model.Training;
import com.nirshal.repository.TrainingRepository;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@GraphQLApi
public class GraphTrainingResource {
    @Inject
    TrainingRepository repository;

    @Query("allTrainings")
    @Description("Get all Trainings stored.")
    public List<Training> getAllTrainings(){
        return repository.getByDates(null, null);
    }

    @Query
    @Description("Get a Trainings by id")
    public Training getTraining(@Name("trainingId") String id) {
        return repository.findById(id);
    }

    @Query("trainingsByDates")
    @Description("Get all Trainings from date to date.")
    public List<Training> getTrainingsByDates(@Name("from") Date from, @Name("to") Date to){
        return repository.getByDates(from, to);
    }

}
