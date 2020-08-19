package com.nirshal.util.data.formatting;

public enum DistanceUnit {
    KM (1000, "Km", 1000.0,"m"),// (16.6667),
    MILES (1609.344, "Miles", 1.0, "Mile"); //(26.8167);

    private final double conversionFactor;
    private final String unitText;
    private final double subUnitMultiplier;
    private final String subUnitText;

    DistanceUnit(double conversionFactor,
                 String unitText,
                 double subUnitMultiplier,
                 String subUnitText){
        this.conversionFactor = conversionFactor;
        this.unitText = unitText;
        this.subUnitMultiplier = subUnitMultiplier;
        this.subUnitText = subUnitText;
    }
    public double getConversionFactor() {
        return conversionFactor;
    }
    public String getUnitText() {
        return unitText;
    }
    public double getSubUnitMultiplier() {return subUnitMultiplier;}
    public String getSubUnitText(){return subUnitText;}

}
