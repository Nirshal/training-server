package com.nirshal.util.decoders;

import com.garmin.fit.*;
import com.nirshal.util.DateManager;
import com.nirshal.util.Semicircles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@ApplicationScoped
public class FIT2TrainingRunDecoder implements LapMesgListener, FileIdMesgListener, SetMesgListener, RecordMesgListener {

//    @Inject
//    Logger logger;
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    Decode decode;
    MesgBroadcaster mesgBroadcaster;

    @PostConstruct
    void init(){
        decode = new Decode();
        //decode.skipHeader();        // Use on streams with no header and footer (stream contains FIT defn and data messages only)
        //decode.incompleteStream();  // This suppresses exceptions with unexpected eof (also incorrect crc)

        mesgBroadcaster = new MesgBroadcaster(decode);
        mesgBroadcaster.addListener((LapMesgListener) this);
        mesgBroadcaster.addListener((FileIdMesgListener) this);
        mesgBroadcaster.addListener((SetMesgListener) this);
    }

    void checkFileIntegrity(File file) throws IOException {
       logger.info("FIT file integrity check...");
        try ( FileInputStream in = new FileInputStream(file) ) {
            if (!decode.checkFileIntegrity(in)) {
                logger.error("FIT file integrity failed!");
                throw new FitRuntimeException("FIT file integrity failed.");
            } else {
                logger.info("FIT file integrity check PASSED!");
            }
        }
    }

    public void decode(File file) throws IOException {
        // Perform a decoder reset (this is absolutely needed when
        // an exception occurred decoding previous file)
        decode.nextFile();

        // Check that the file is correct
        checkFileIntegrity(file);

        logger.info("Start Decoding...");
        try ( FileInputStream in = new FileInputStream(file) )
        {
            decode.read(in, mesgBroadcaster, mesgBroadcaster);
            logger.info("Decoded FIT file ");
        }
    }

    // Get info for intervals from lap messages.
    @Override
    public void onMesg(LapMesg lapMesg) {
//        if (training == null) {
        switch (lapMesg.getSport()){
            case RUNNING:
                logger.info("RUNNING");
//                    this.training = new TrainingRun();
//                    this.training.setDate(creationDate);
                break;
            case SWIMMING:
                logger.info("SWIMMING");
//
//                    this.training = new TrainingSwim();
//                    this.training.setDate(creationDate);
                break;
            case CYCLING:
                logger.info("CYCLING");

//                    this.training = new TrainingBike();
//                    this.training.setDate(creationDate);
                break;
            case TRAINING:
                logger.info("TRAINING");
//
//                    this.training = new TrainingWorkout();
//                    this.training.setDate(creationDate);
                break;
            default:
                logger.info("UNKNOWN");

//                    this.training = new TrainingUnknown();
//                    this.training.setDate(creationDate);
                break;
        }
//        }
//        Interval interval = new Interval(
        logger.info( "DISTANCE: " + lapMesg.getTotalDistance() +
                " TIME: " + lapMesg.getTotalTimerTime());
//                training.getType());
//        if (lapMesg.getSport() == Sport.SWIMMING) interval.setSwimStroke(lapMesg.getSwimStroke());
//        training.add(interval);
    }
    @Override
    public void onMesg(FileIdMesg fileIdMesg) {

//        if (creationDate == null) {
        logger.info(DateManager.getLocalizedDateTimeFromGarminTimestamp(fileIdMesg.getTimeCreated().getTimestamp(), "Europe/Rome").toString());
//        }
//        System.out.println(creationDate);
    }
    // TODO: FIX this
    @Override
    public void onMesg(SetMesg setMesg) {
//        if (this.training == null) {
//            this.training = new TrainingWorkout();
//            this.training.setDate(creationDate);
//        }
        logger.info("---");
        Integer[] pippo = setMesg.getCategory();
        Integer[] subpippo = setMesg.getCategorySubtype();
        if (pippo!=null){
            for (Integer i: pippo){
                logger.info(ExerciseCategory.getStringFromValue(i));
            }
            if (subpippo!=null){
                for (Integer i: pippo){
                    logger.info(i.toString());
                }
            }

            logger.info("Tempo: " + setMesg.getDuration());
            logger.info("Ripetizioni: " + setMesg.getRepetitions());
            if (setMesg.getWeight() != null && setMesg.getWeight() != 0.0) {
                logger.info("Peso: " + setMesg.getWeight());
            }
//            Interval interval = new Interval(
//                    setMesg.getRepetitions(),
//                    setMesg.getDuration(),
//                    training.getType());
//            interval.setExercise(ExerciseCategory.getStringFromValue(setMesg.getCategory()[0]));
//            training.add(interval);

        } else {
            logger.info("Rec.");
            logger.info("Tempo: " + setMesg.getDuration());
//            Interval interval = new Interval(
//                    0,
//                    setMesg.getDuration(),
//                    training.getType());
//            interval.setExercise("Rec.");
//            training.add(interval);
        }
    }

    @Override
    public void onMesg(RecordMesg mesg) {
//            System.out.println("Record:");

//            printValues(mesg, RecordMesg.TimestampFieldNum);
//            printValues(mesg, RecordMesg.HeartRateFieldNum);
//            printValues(mesg, RecordMesg.CadenceFieldNum);
//            printValues(mesg, RecordMesg.DistanceFieldNum);
//            printValues(mesg, RecordMesg.SpeedFieldNum);
//            printValues(mesg, RecordMesg.AltitudeFieldNum);
//            printValues(mesg, RecordMesg.PositionLatFieldNum);
//            printValues(mesg, RecordMesg.PositionLongFieldNum);
//            printValues(mesg, RecordMesg.GpsAccuracyFieldNum);
//

        logger.info(
                "[ " + mesg.getTimestamp() + " ] - "
                        + mesg.getDistance()/1000 + " Km - "
                        + mesg.getSpeed() * 3.6 + " Km/h - "
                        + Semicircles.getDegrees(mesg.getPositionLat())
                        + " " + Semicircles.getDegrees(mesg.getPositionLong())
        );

//            printDeveloperData(mesg);
    }
}

