package com.nirshal.util.excel.renderers;

public enum SpeedUnit {
    SI_RUNNING(3.6, "Km/h"),// (16.6667),
    IMPERIAL_RUNNING(3600/1609.344, "MPH"), //(26.8167);
    SI_SWIMMING (1, "m/s"),// (16.6667),
    SI_CYCLING (3.6, "Km/h");

    private final double conversionFactor;
    private final String unitText;


    SpeedUnit(double conversionFactor,
             String unitText) {
        this.conversionFactor = conversionFactor;
        this.unitText = unitText;

    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public String getUnitText() {
        return unitText;
    }

}
