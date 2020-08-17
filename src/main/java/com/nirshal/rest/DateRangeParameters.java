package com.nirshal.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeParameters {

    public static final String PARAM_DATE_FROM = "from";
    public static final String PARAM_DATE_TO = "to";

    /**
     * The date used as start for the query
     */
    @QueryParam(PARAM_DATE_FROM)
    String from;

    /**
     * The date used as end for the query
     */
    @QueryParam(PARAM_DATE_TO)
    String to;

    /**
     * Convenience Getter to convert string into date.
     * @return
     */
    public Date getFromDate(){
        return toDate(this.from);
    }

    /**
     * Convenience Getter to convert string into date.
     * @return
     */
    public Date getToDate(){
        return toDate(this.to);
    }

    private Date toDate(String text){

        if (text == null) return null;

        String[] components = text.split("-");
        Calendar calendar = Calendar.getInstance();

        calendar.set
                (
                        Integer.parseInt(components[0]),
                        Integer.parseInt(components[1])-1,
                        Integer.parseInt(components[2]),
                        0,
                        0,
                        0
                );

        return calendar.getTime();

    }
}
