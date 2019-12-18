package com.nirshal.util.decoders;

import com.garmin.fit.*;
import com.nirshal.model.Lap;
import com.nirshal.model.Record;
import com.nirshal.model.Set;
import com.nirshal.model.Training;
import com.nirshal.util.DateManager;
import com.nirshal.util.Semicircles;
import com.nirshal.util.mongodb.MongoCollections;
import com.nirshal.util.mongodb.MongoRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

@ApplicationScoped
@Data
public class FIT2TrainingRunDecoder implements TrainingFileDecoder, LapMesgListener, FileIdMesgListener, SetMesgListener, RecordMesgListener {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    Decode decode;
    MesgBroadcaster mesgBroadcaster;

    @Inject
    MongoCollections collections;

    MongoRepository<Training> trainingRepository;

    Training training;

    @PostConstruct
    void init(){
        trainingRepository = collections.getRepositoryFrom(Training.class);
        decode = new Decode();
        //decode.skipHeader();        // Use on streams with no header and footer (stream contains FIT defn and data messages only)
        //decode.incompleteStream();  // This suppresses exceptions with unexpected eof (also incorrect crc)

        mesgBroadcaster = new MesgBroadcaster(decode);
        mesgBroadcaster.addListener((FileIdMesgListener) this);
        mesgBroadcaster.addListener((LapMesgListener) this);
        mesgBroadcaster.addListener((SetMesgListener) this);
        mesgBroadcaster.addListener((RecordMesgListener) this);
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

    public Training decode(File file) throws IOException {
        // Perform a decoder reset (this is absolutely needed when
        // an exception occurred decoding previous file)
        decode.nextFile();

        // Check that the file is correct
        checkFileIntegrity(file);

        logger.info("Start Decoding...");
        try ( FileInputStream in = new FileInputStream(file) )
        {
            training = new Training();
            decode.read(in, mesgBroadcaster, mesgBroadcaster);
            logger.info("Decoded FIT file ");
            trainingRepository.upsert(training);
            return training;
        }
    }

    @Override
    public void onMesg(LapMesg m) {
        logger.info(
                "ADDING LAP: SPORT={}, DISTANCE={}, TIME={} ",
                m.getSport().name(),
                m.getTotalDistance(),
                m.getTotalTimerTime()
        );
        training.getLaps().add(
                new Lap(
                        DateManager.getLocalizedDateTimeFromGarminTimestamp(m.getTimestamp().getTimestamp(), "Europe/Rome"),
                        DateManager.getLocalizedDateTimeFromGarminTimestamp(m.getStartTime().getTimestamp(),"Europe/Rome"),
                        m.getSport().name(),
                        m.getTotalDistance() == null ? null : m.getTotalDistance().doubleValue(),
                        m.getTotalMovingTime() == null ? null : m.getTotalMovingTime().doubleValue(),
                        m.getTotalTimerTime() == null ? null : m.getTotalTimerTime().doubleValue(),
                        m.getTotalElapsedTime() == null ? null : m.getTotalElapsedTime().doubleValue(),
                        m.getAvgCadence() == null ? null : m.getAvgCadence().intValue(),
                        m.getMaxCadence() == null ? null : m.getMaxCadence().intValue(),
                        m.getAvgHeartRate() == null ? null : m.getAvgHeartRate().intValue(),
                        m.getMinHeartRate() == null ? null : m.getMinHeartRate().intValue(),
                        m.getMaxHeartRate() == null ? null : m.getMaxHeartRate().intValue(),
                        m.getTotalCalories(),
                        m.getAvgRunningCadence() == null ? null : m.getAvgRunningCadence().intValue(),
                        m.getMaxRunningCadence() == null ? null : m.getMaxRunningCadence().intValue(),
                        m.getAvgSpeed() == null ? null : m.getAvgSpeed().doubleValue(),
                        m.getMaxSpeed() == null ? null : m.getMaxSpeed().doubleValue(),
                        m.getAvgStepLength() == null ? null : m.getAvgStepLength().doubleValue(),
                        m.getStartPositionLat() == null ? null : Semicircles.getDegrees(m.getStartPositionLat()),
                        m.getEndPositionLat() == null ? null : Semicircles.getDegrees(m.getEndPositionLat()),
                        m.getStartPositionLong() == null ? null : Semicircles.getDegrees(m.getStartPositionLong()),
                        m.getEndPositionLong() == null ? null : Semicircles.getDegrees(m.getEndPositionLong()),
                        m.getGpsAccuracy() == null ? null : m.getGpsAccuracy().intValue()
                )
        );
    }

    @Override
    public void onMesg(FileIdMesg fileIdMesg) {
        training.setCreationDate(DateManager.getLocalizedDateTimeFromGarminTimestamp(fileIdMesg.getTimeCreated().getTimestamp(), "Europe/Rome"));
        logger.info("File creation date: {}", training.getCreationDate());
    }

    @Override
    public void onMesg(SetMesg s) {

        training.getSets().add(
                new Set(
                        SetType.getStringFromValue(s.getSetType()),
                        (s.getCategory() == null ? null : ExerciseCategory.getStringFromValue(Arrays.asList(s.getCategory()).get(0))),
                        DateManager.getLocalizedDateTimeFromGarminTimestamp(s.getStartTime().getTimestamp(),"Europe/Rome"),
                        DateManager.getLocalizedDateTimeFromGarminTimestamp(s.getTimestamp().getTimestamp(),"Europe/Rome"),
                        s.getDuration() == null ? null : s.getDuration().doubleValue(),
                        s.getRepetitions(),
                        s.getWeight() == null ? null : s.getWeight().doubleValue()
//                        s.getWeightDisplayUnit() == null ? null : Weight.getStringFromValue(s.getWeightDisplayUnit()) // NOT USED BY GARMIN?
                )
        );
        logger.info(
                "ADDING SET: {}",
                training.getSets().get(training.getSets().size()-1)
        );
    }

    @Override
    public void onMesg(RecordMesg m) {

        logger.info( "ADDING RECORD: [{}] - Distance: {} Km - Speed: {} - Latitude: {} - Longitude: {}",
                m.getTimestamp(),
                m.getDistance() == null ? null : m.getDistance()/1000,
                m.getSpeed() == null ? null : m.getSpeed() * 3.6,
                m.getPositionLat() == null ? null : Semicircles.getDegrees(m.getPositionLat()),
                m.getPositionLong() == null ? null : Semicircles.getDegrees(m.getPositionLong())
        );

        training.getRecords().add(
                new Record(
                        DateManager.getLocalizedDateTimeFromGarminTimestamp(m.getTimestamp().getTimestamp(),"Europe/Rome"),
                        m.getDistance() == null ? null : m.getDistance().doubleValue(),

                        m.getSpeed() == null ? null : m.getSpeed().doubleValue(),
                        m.getEnhancedSpeed() == null ? null : m.getEnhancedSpeed().doubleValue(),
                        m.getHeartRate() == null ? null : m.getHeartRate().intValue(),
                        m.getCalories(),

                        m.getCadence() == null ? null : m.getCadence().intValue(),
                        m.getStepLength() == null ? null : m.getStepLength().doubleValue(),

                        m.getPositionLat() == null ? null : Semicircles.getDegrees(m.getPositionLat()),
                        m.getPositionLong() == null ? null : Semicircles.getDegrees(m.getPositionLong()),
                        m.getAltitude() == null ? null : m.getAltitude().doubleValue(),
                        m.getEnhancedAltitude() == null ? null : m.getEnhancedAltitude().doubleValue(),
                        m.getGpsAccuracy() == null ? null : m.getGpsAccuracy().intValue()
                )
        );
    }
}

