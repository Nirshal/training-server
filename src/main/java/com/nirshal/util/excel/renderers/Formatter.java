package com.nirshal.util.excel.renderers;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Provides reference for human readable display of data in the context of this app.
 */
public class Formatter {
    /**
     * Convert a Double representing a duration to a human readable string representation.
     * @param duration in seconds, supporting milliseconds.
     * @return a String containing a time or duration in the format H:MM:SS.mmm
     */
    public static String asTime(Double duration){
        int totalSeconds = duration.intValue();

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600)/60;
        int seconds = totalSeconds % 60;
        int millis = (int) (( duration - totalSeconds )*1000);

        if (hours != 0){
            return String.format("%d:%02d:%02d.%03d", hours, minutes, seconds, millis);
        } else {
            return String.format("%01d:%02d.%03d", minutes, seconds, millis);
        }
    }
    public static String asPace(Double durationPerUnit, PaceUnit unit){
        return Formatter.asTime(durationPerUnit) + " " + unit.getUnitText();
    }
    public static String asSpeed(Double speed, SpeedUnit unit){
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(0);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        String printSpeedString = nf.format(speed);

        return printSpeedString + " " + unit.getUnitText();

    }
    public static String asDistance(Double distance, DistanceUnit unit){
        double printDistanceValue = (distance < 1.0 ?
                distance*unit.getSubUnitMultiplier()
                :
                distance);
        String printUnit = (distance < 1.0 ?
                unit.getSubUnitText()
                :
                unit.getUnitText());
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(0);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        String printDistanceString = nf.format(printDistanceValue);

        return printDistanceString + " " + printUnit;

    }

}
