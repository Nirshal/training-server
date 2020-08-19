package com.nirshal.rest;

import com.garmin.fit.FitRuntimeException;
import com.nirshal.model.TrainingInfo;
import com.nirshal.util.files.FilesLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/dev")
@Produces(MediaType.APPLICATION_JSON)
public class DevResource {

    @Inject
    FilesLoaderService filesLoaderService;

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @POST
    @Path("/mass_import")
    public List<TrainingInfo> massImport(String path) throws IOException, FitRuntimeException {
        logger.info("Received request to mass import file/s from {}.", path);

        return filesLoaderService.loadFiles(path);
    }

}