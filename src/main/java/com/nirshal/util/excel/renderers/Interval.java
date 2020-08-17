package com.nirshal.util.excel.renderers;

import com.garmin.fit.SwimStroke;
import lombok.*;

import java.time.format.DateTimeParseException;

@EqualsAndHashCode
@AllArgsConstructor
public class Interval
{
    @NonNull
    @Setter @Getter private UnitsType outputUnit;
    @Getter(AccessLevel.PRIVATE) private double meters;
    @Getter(AccessLevel.PRIVATE) private double seconds;
    public double getNormalisedSpeed()
    {
        return this.getMeters()/this.getSeconds();
    }
    @Setter @Getter private SwimStroke swimStroke = null;
    @Setter @Getter private String exercise = null;


    public double getDistanceInMeters(){
        return getMeters();
    }
    public double getDistance() { return getMeters()/ outputUnit.getDistanceUnit().getConversionFactor(); }
    public double getTime() { return seconds; }

    public double getPace(){ return outputUnit.getPaceUnit().getConversionFactor()/getNormalisedSpeed(); }
    public double getSpeed(){
        return outputUnit.getSpeedUnit().getConversionFactor()*getNormalisedSpeed();
    }
    public String getDistanceString(){
        return Formatter.asDistance(getDistance(),outputUnit.getDistanceUnit());
    }
    public String getTimeString(){
        return Formatter.asTime(getSeconds());
    }
    public String getPaceString(){
        return Formatter.asPace(getPace(), outputUnit.getPaceUnit());
    }
    public String getSpeedString(){
        return Formatter.asSpeed(getSpeed(), outputUnit.getSpeedUnit());
    }

    public boolean isRecovery(){
        return (isFasterThan(this.getOutputUnit().getRecoverySpeed()));
    }

    public String toString()
    {
        return
                getDistanceString() + " in " +
                        getTimeString() + " = " +
                        getPaceString() + " or " +
                        getSpeedString();
    }

    /**
     *  Constructor to parse data from strings using Double and Duration parsing.
     *
     * @param distanceKmString a string indicating the Kms (and not the meter!).
     * @param timeToParse a String representing a duration, in the form HH:MM:SS.mmm
     * @throws IllegalArgumentException if one of the parsing fails.
     */
    public Interval(String distanceKmString, String timeToParse, UnitsType outputUnit) throws IllegalArgumentException {
        try
        {
            this.meters = 1000.0 * Double.parseDouble(distanceKmString);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Impossible to parse Distance", e);
        }
        try
        {
            String[] duration = timeToParse.split(":");
            this.seconds = Double.parseDouble(duration[0])*3600.0
                    + Double.parseDouble(duration[1])*60.0
                    + Double.parseDouble(duration[2]);
        }
        catch (DateTimeParseException e)
        {
            throw new IllegalArgumentException("Impossible to parse Time", e);
        }
        this.outputUnit = outputUnit;
    }
    public Interval(double meters, double seconds, UnitsType outputUnit) {
        this.meters = meters;
        this.seconds = seconds;
        this.outputUnit = outputUnit;
    }

    public boolean isFasterThan(double metersPerSeconds){
        return getNormalisedSpeed() > metersPerSeconds;
    }
}
