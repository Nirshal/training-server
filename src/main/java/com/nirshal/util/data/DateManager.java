package com.nirshal.util.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateManager {

    public static DateTimeFormatter outputDateFormatCSV = DateTimeFormatter.ofPattern("dd/MM/YYYY");

    /**
     * Parse a date from a text with a predefined format.
     * @param text can either be "dd/MM/YYYY" OR "dd/MM/YYYY:" with the terminating semicolon.
     * @return
     */
    public static LocalDateTime parse(String text){

        String textToParse = (text.endsWith(":") ? dropLast(text) : text);

        String [] elements = textToParse.split("/");

        int year = Integer.valueOf(elements[2]);
        int month = Integer.valueOf(elements[1]);
        int day = Integer.valueOf(elements[0]);

        return LocalDateTime.of(
                year,
                month,
                day,
                0,
                0,
                0,
                0);
    }
    public static LocalDateTime zeroingTime(LocalDateTime date){
        return date.truncatedTo(ChronoUnit.DAYS);
    }
    public static LocalDateTime getLocalizedDateTimeFromGarminTimestamp(long garminTimestamp, String locale){
        String garminBaseTimestamp = "1989-12-31T00:00:00.000Z";
        return LocalDateTime
                .ofInstant(
                        Instant
                                .parse(garminBaseTimestamp)
                                .plusSeconds(garminTimestamp)
                        , ZoneId.of(locale));
    }

    private static String dropLast(String text){
        return text.subSequence(0, text.length()-1).toString();
    }
    public static Date localDateTimeToDate(LocalDateTime localDateTime){
       return Date.from(localDateTime.atZone(ZoneId.of("Europe/Rome")).toInstant());//toInstant(ZoneOffset..of("Europe/Rome")));
    }
    public static LocalDateTime dateToLocalDateDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("Europe/Rome"));
    }
}
