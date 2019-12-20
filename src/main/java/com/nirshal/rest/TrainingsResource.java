package com.nirshal.rest;

import com.garmin.fit.FitRuntimeException;
import com.nirshal.model.Training;
import com.nirshal.services.TrainingService;
import com.nirshal.util.mongodb.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Path("/trainings")
@Produces(MediaType.APPLICATION_JSON)
public class TrainingsResource {

    @Inject
    TrainingService trainingService;

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @GET
    public Training get(@QueryParam(RestConstants.TRAINING_ID) String trainingId) {
        logger.info("Received request to fetch training with id={}.", trainingId);
        return trainingService.get(trainingId);
    }
    @GET
    @Path("/list")
    public List<Training> get(@BeanParam @Valid  Page page) {
        logger.info("Received request to fetch trainings page={}.", page);
        return trainingService.get(page);
    }
    @POST
    public Training add(File file) throws IOException, FitRuntimeException {
        logger.info("Received request to add a file.");
        return trainingService.add(file);
    }
    @PUT
    public Training update(Training training){
        logger.info("Received request to update a training.");
        return trainingService.update(training);
    }
    @DELETE
    public Training update(@QueryParam(RestConstants.TRAINING_ID) String trainingId){
        logger.info("Received request to delete a training.");
        return trainingService.delete(trainingId);
    }

}