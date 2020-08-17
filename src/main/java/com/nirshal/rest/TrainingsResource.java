package com.nirshal.rest;

import com.garmin.fit.FitRuntimeException;
import com.nirshal.model.Training;
import com.nirshal.model.TrainingInfo;
import com.nirshal.services.TrainingService;
import com.nirshal.services.util.FileContainer;
import com.nirshal.util.mongodb.Page;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
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
    @Path("/file")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@QueryParam(RestConstants.TRAINING_ID) String trainingId) throws IOException {
        logger.info("Received request to fetch training with id={}.", trainingId);
        FileContainer file = trainingService.getFile(trainingId);
        return Response.ok(file.getFile(), MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"" ) //optional
                .build();
    }

    @GET
    @Path("/list")
    public List<TrainingInfo> get(@BeanParam @Valid  Page page) {
        logger.info("Received request to fetch trainings list page={}.", page);
        return trainingService.get(page);
    }

    @POST
    public Training add(File file) throws IOException, FitRuntimeException {
        // TODO: add option to control overwriting of existing activities.
        logger.info("Received request to add a file.");

        return trainingService.add(file);
    }
    @PUT
    public Training update(Training training){
        logger.info("Received request to update a training.");
        return trainingService.update(training);
    }
    @DELETE
    public Training delete(@QueryParam(RestConstants.TRAINING_ID) String trainingId){
        logger.info("Received request to delete a training.");
        return trainingService.delete(trainingId);
    }

    @GET
    @Path("/range")
    public List<TrainingInfo> getByDates(@BeanParam @Valid DateRangeParameters rangeParameters){
        logger.info("Received request to fetch a list of Training between dates.");
        return trainingService.getByDates(rangeParameters.getFromDate(),rangeParameters.getToDate());
    }

    @PATCH
    @Path("/{"+ RestConstants.TRAINING_ID +"}/description")
    public TrainingInfo setDescription(
            @PathParam(RestConstants.TRAINING_ID) @NonNull @NotEmpty String trainingId,
            String body
    )
    {
        logger.info("Received request to update description for training with id={}.", trainingId);
        return trainingService.update(trainingId, body, null);
    }

    @PATCH
    @Path("/{"+ RestConstants.TRAINING_ID +"}/comments")
    public TrainingInfo setComments(
            @PathParam(RestConstants.TRAINING_ID) @NonNull @NotEmpty String trainingId,
            String body
    )
    {
        logger.info("Received request to update comments for training with id={}.", trainingId);
        return trainingService.update(trainingId, null, body);
    }

}