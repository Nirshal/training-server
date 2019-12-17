package com.nirshal;

import com.garmin.fit.FitRuntimeException;
import com.nirshal.util.decoders.FIT2TrainingRunDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;

@Path("/trainings")
public class TrainingsResource {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    FIT2TrainingRunDecoder decoder;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @POST
    public void load(File file) throws IOException, FitRuntimeException {
        logger.info("Received file to decode.");
        decoder.decode(file);
    }
}