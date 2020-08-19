package com.nirshal.util.excel;

import com.nirshal.model.Lap;
import com.nirshal.model.Set;
import com.nirshal.model.Training;
import com.nirshal.util.containers.Table;
import com.nirshal.util.data.DateManager;
import com.nirshal.util.data.formatting.DistanceUnit;
import com.nirshal.util.data.formatting.Formatter;
import com.nirshal.util.data.formatting.SwimStrokeData;
import com.nirshal.util.data.formatting.UnitsType;
import org.apache.poi.ss.usermodel.CellType;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class Renderer {

    static final int ROW_1 = 0;
    static final int ROW_2 = 1;
    static final int ROW_3 = 2;
    static final int ROW_4 = 3;
    static final int WEEK_COLUMN = 0;
    static final int DATE_COLUMN = 1;
    static final int DESCRIPTION_COLUMN = 2;
    static final int COMMENTS_COLUMN = 3;
    static final int TOTAL_TIME_COLUMN = 4;
    static final int TOTAL_DISTANCE_COLUMN = 5;
    static final int AVG_RHYTHM_COLUMN = 6;
    static final int SPLIT_START_COLUMN = 7;

    public static Table<XLSOutputCell> emptyPlaceholder(LocalDateTime localDateTime) {
        Table<XLSOutputCell> table = new Table<>(XLSOutputCell.EMPTY);

        return table
                .put(ROW_1, DATE_COLUMN, dateCell(localDateTime))
                .put(ROW_1, DESCRIPTION_COLUMN, textCell(UnitsType.EMPTY.getDefaultDescription()));
    }

    public static Table<XLSOutputCell> toXLSRecord(Training training) {
        UnitsType splitType = UnitsType.getFromSport(training.getSport());
        Table<XLSOutputCell> table = new Table<>(XLSOutputCell.EMPTY);
        //0 - Settimana -> vuota.
        table
                //1 - Data;
                .put(ROW_1, DATE_COLUMN, dateCell(training.getCreationDate()))
                //2 - Allenamento
                .put(
                        ROW_1, DESCRIPTION_COLUMN,
                        textCell(
                                splitType == UnitsType.WORKOUT ?
                                        training
                                                .getSets().stream()
                                                .filter( set -> set.getType().equals("ACTIVE"))
                                                .map(Set::getExercise)
                                                .collect(Collectors.toSet())
                                                .stream()
                                                .collect(Collectors.joining(", ", UnitsType.WORKOUT.name() + ": ", "."))
                                        :
                                        training.getDescription()
                        )
                )
                //3 - Sensazioni
                .put(ROW_1, COMMENTS_COLUMN, textCell(training.getComments()))
                //4 - Tempo Totale
                .put(ROW_1, TOTAL_TIME_COLUMN,
                        timeCell(training.getTotalTime(), true))
                // 5 - Distanza Totale
                .put(ROW_1, TOTAL_DISTANCE_COLUMN,
                        splitType == UnitsType.WORKOUT ?
                                workoutRepCell(
                                        training.getSets().stream().filter(set -> set.getType().equals("ACTIVE"))
                                                .map(Set::getRepetitions).reduce(0, Math::addExact), true
                                )
                                :
                                distanceCell(training.getTotalDistance(), true))
                //6 - Ritmo Medio
                .put(ROW_1, AVG_RHYTHM_COLUMN,
                        splitType == UnitsType.WORKOUT ?
                                textCell("")
                                :
                                avgRhythmCell(training.getAvgSpeed(), training.getSport(), true));
        //7 - Splits
        switch (splitType){
            case WORKOUT:
                addWorkoutSplits(table, training);
            case SWIMMING:
                return addSwimSplits(table, training);
            default:
                return addSplits(table,training);
        }
    }

    private static Table<XLSOutputCell> addSplits(Table<XLSOutputCell> table, Training training){
        Integer splitColumn = SPLIT_START_COLUMN;
        for  (Lap lap : training.getLaps()) {
            table
                    .put(ROW_1, splitColumn, splitDistanceCell(lap.getDistance()))
                    .put(ROW_2, splitColumn, timeCell(lap.getTotalTimerTime(), false))
                    .put(ROW_3, splitColumn, avgRhythmCell(lap.getDistance()/lap.getTotalTimerTime(), lap.getSport(), false));
            splitColumn++;
        }
        return table;
    }

    private static Table<XLSOutputCell> addSwimSplits(Table<XLSOutputCell> table, Training training){
        Integer splitColumn = SPLIT_START_COLUMN;
        for  (Lap lap : training.getLaps()) {
            // Discriminate if this split is rest or active:
            if (lap.getDistance() == 0.0) {
                table
                        .put(ROW_1, splitColumn, recoveryCell())
                        .put(ROW_2, splitColumn, timeCell(lap.getTotalTimerTime(), false));
            } else {
                table
                        .put(ROW_1, splitColumn, splitDistanceCell(lap.getDistance()))
                        .put(ROW_2, splitColumn, swimStrokeCell(lap.getSwimStroke()))
                        .put(ROW_3, splitColumn, timeCell(lap.getTotalTimerTime(), false))
                        .put(ROW_4, splitColumn,  avgRhythmCell(lap.getDistance() / lap.getTotalTimerTime(), lap.getSport(), false));
            }
            splitColumn++;
        }
        return table;
    }

    private static Table<XLSOutputCell> addWorkoutSplits(Table<XLSOutputCell> table, Training training){
        Integer splitColumn = SPLIT_START_COLUMN;
        Integer split = 0;
        for  (Set set : training.getSets()) {
            // Discriminate if this set is rest or active:
            if (set.getType().equals("REST")) {
                table
                        .put(ROW_1, splitColumn, recoveryCell())
                        .put(ROW_2, splitColumn, timeCell(set.getDuration(), false));
            } else {
                table
                        .put(ROW_1, splitColumn, seriesCell(++split))
                        .put(ROW_2, splitColumn, workoutRepCell(set.getRepetitions(), false));
            }
            splitColumn++;
        }
        return table;
    }

    private static XLSOutputCell dateCell(LocalDateTime localDateTime){
        return new XLSOutputCell(
                CellType.NUMERIC,
                StyleTypes.DATE,
                DateManager.localDateTimeToDate(DateManager.zeroingTime(localDateTime))
        );
    }
    private static XLSOutputCell textCell(String text){
        return new XLSOutputCell(
                CellType.STRING,
                StyleTypes.TEXT,
                text
        );
    }
    private static XLSOutputCell timeCell(Double time, Boolean centered){
        return new XLSOutputCell(
                CellType.STRING,
                centered? StyleTypes.CENTERED_TEXT : StyleTypes.TEXT,
                Formatter.asTime(time)
        );
    }
    private static XLSOutputCell distanceCell(Double distance, Boolean centered){
        return new XLSOutputCell(
                CellType.STRING,
                centered? StyleTypes.CENTERED_TEXT : StyleTypes.TEXT,
                Formatter.asDistance(distance, DistanceUnit.KM)
        );
    }
    private static XLSOutputCell splitDistanceCell(Double distance){
        return new XLSOutputCell(
                CellType.STRING,
                StyleTypes.SUB_HEADER,
                Formatter.asDistance(distance, DistanceUnit.KM)
        );
    }
    private static XLSOutputCell avgRhythmCell(Double speed, String sport, Boolean centered) {
        return new XLSOutputCell(
                CellType.STRING,
                centered? StyleTypes.CENTERED_TEXT : StyleTypes.TEXT,
                Formatter.asRhythmDefaultPerSport(speed, sport)
        );
    }
    private static XLSOutputCell recoveryCell(){
        return new XLSOutputCell(
                CellType.STRING,
                StyleTypes.SUB_HEADER,
                "Rec.");
    }
    private static XLSOutputCell swimStrokeCell(String stroke){
        return new XLSOutputCell(
                CellType.STRING,
                StyleTypes.SUB_HEADER,
                SwimStrokeData.getText(stroke)
        );
    }
    private static XLSOutputCell workoutRepCell(Integer repetitions, Boolean centered){
        return new XLSOutputCell(
                CellType.STRING,
                centered ? StyleTypes.CENTERED_TEXT : StyleTypes.TEXT,
                repetitions + " Rip.");
    }
    private static XLSOutputCell seriesCell(Integer seriesNumber){
        return new XLSOutputCell(
                CellType.STRING,
                StyleTypes.SUB_HEADER,
                "Serie " + seriesNumber
        );
    }

}
