package com.nirshal.util.data.formatting;

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
        int millis = (int) (( duration - totalSeconds ) * 1000 );

        if (hours != 0){
            return String.format("%d:%02d:%02d.%03d", hours, minutes, seconds, millis);
        } else {
            return String.format("%01d:%02d.%03d", minutes, seconds, millis);
        }
    }
    public static String asPace(Double speed, PaceUnit unit){

        Double durationPerUnit = unit.getConversionFactor()/speed;
        return Formatter.asTime(durationPerUnit) + " " + unit.getUnitText();
    }

    public static String asRhythmDefaultPerSport(Double speed, String sport){
        UnitsType unitType = UnitsType.getFromSport(sport);

        switch (unitType){
            case CYCLING:
                return asSpeed(speed, unitType.getSpeedUnit());
            default:
                return asPace(speed, unitType.getPaceUnit());
        }

    }

    public static String asSpeed(Double speed, SpeedUnit unit){
        Double unitSpeed = speed * unit.getConversionFactor();
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(0);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        String printSpeedString = nf.format(unitSpeed);

        return printSpeedString + " " + unit.getUnitText();
    }

    public static String asDistance(Double distanceMeter, DistanceUnit unit){
        double distance = distanceMeter/unit.getSubUnitMultiplier();
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
