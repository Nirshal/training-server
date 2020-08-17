package com.nirshal.util.excel.renderers;

public enum PaceUnit {
    SI_RUNNING(1000, "min/Km"),// (16.6667),
    IMPERIAL_RUNNING(1609.344, "min/Mile"), //(26.8167);
    SI_SWIMMING (100, "min/100m"),// (16.6667),
    SI_CYCLING (1000, "min/Km");

    private final double conversionFactor;
    private final String unitText;


    PaceUnit(double conversionFactor,
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



