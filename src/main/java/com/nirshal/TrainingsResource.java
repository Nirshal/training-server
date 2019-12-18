package com.nirshal;

import com.garmin.fit.FitRuntimeException;
import com.nirshal.model.Training;
import com.nirshal.util.decoders.TrainingFileDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;

@Path("/trainings")
@Produces(MediaType.APPLICATION_JSON)
public class TrainingsResource {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    TrainingFileDecoder decoder;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @POST
    public Training load(File file) throws IOException, FitRuntimeException {
        logger.info("Received file to decode.");
        return decoder.decode(file);
    }
}