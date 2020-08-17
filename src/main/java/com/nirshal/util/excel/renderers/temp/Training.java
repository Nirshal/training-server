package com.nirshal.util.excel.renderers.temp;

import com.nirshal.tutorials.encoderdecoder.CSVPrintable;
import com.nirshal.tutorials.utils.DateManager;
import com.nirshal.tutorials.xls.XLSPrintable;
import com.nirshal.util.excel.renderers.Interval;
import com.nirshal.util.excel.renderers.UnitsType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public abstract class Training extends ArrayList<Interval> implements Comparable<Training>, CSVPrintable, XLSPrintable {

    @Getter @Setter
    private LocalDateTime date = null;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-YYYY");
    @Getter @Setter
    private String description = getType().getDefaultDescription();
    @Getter @Setter
    private String comment = "";

    public LocalDateTime getReferenceDate(){
        return DateManager.zeroingTime(this.getDate());
    }

    public String getReferenceDateString() {
        return this.getReferenceDate().format(DateManager.outputDateFormatCSV);
    }
    public abstract UnitsType getType();

    private Interval getTotalInteval(){
        return new Interval(this.getTotalDistanceInMeters(), this.getTotalTime(), this.getType());
    }
    private Interval getFilteredTotalInterval(){
        Training filtered = filterRecoveries();
        return new Interval(filtered.getTotalDistanceInMeters(), filtered.getTotalTime(), filtered.getType());

    }
    private double getTotalDistanceInMeters(){
        return this.stream().map(Interval::getDistanceInMeters).reduce(0.0,(a,b)-> a+b);
    }
    private double getTotalTime(){
        return this.stream().map(Interval::getTime).reduce(0.0,(a,b)-> a+b);
    }


    public double getTotalDistance(){
        return this.getTotalInteval().getDistance();

    }
    public String getTotalDistanceString(){
        return this.getTotalInteval().getDistanceString();//Formatter.asDistance(getTotalDistance(), this.getType().getDistanceUnit());
    }
    public String getTotalTimeString(){
        return this.getTotalInteval().getTimeString();
    }
    public String getActiveTimeString(){
        return this.getFilteredTotalInterval().getTimeString();
    }
    public String getMeanSpeedString(){
        return this.getFilteredTotalInterval().getSpeedString();
    }
    public String getMeanPaceString(){
        return this.getFilteredTotalInterval().getPaceString();
    }

//    public Training filtered(Boolean faster, double metersPerSeconds) {
//        Training filtered = (Training) this.clone();
//        filtered.clear();
//        this.stream()
//                .filter( u -> (!faster ^ u.isFasterThan(metersPerSeconds)))
//                .forEach(filtered::add);
//        return filtered;
//    }
    public Training filterRecoveries() {
        Training filtered = (Training) this.clone();
        filtered.clear();
        this.stream()
                .filter(Interval::isRecovery)
                .forEach(filtered::add);
        return filtered;
    }

    public void printIntervals() {
        this
                .stream()
                .forEach(System.out::println);
    }

    @Override
    public int compareTo(Training o) {
        return this.getDate().compareTo(o.getDate());

    }

    @Override
    public Date getXLSFormatDate() {
        return DateManager.localDateTimeToDate(this.getReferenceDate());
    }


}

