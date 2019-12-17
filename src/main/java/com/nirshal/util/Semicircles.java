package com.nirshal.util;

/**
 * Perform coordinates conversion between Circles and Degrees and vice-versal
 */
public class Semicircles {

    public static final Double MAX_INTEGER = Math.pow(2,31);
    public static final Integer SEMICIRCLE_DEGREES = 180;

    public static Integer fromDegrees(Double degrees){
        return (int) ((degrees * MAX_INTEGER) / SEMICIRCLE_DEGREES.doubleValue());
    }

    public static Double getDegrees(Integer circles){
        return (circles.doubleValue() * SEMICIRCLE_DEGREES.doubleValue())/MAX_INTEGER ;
    }
}
