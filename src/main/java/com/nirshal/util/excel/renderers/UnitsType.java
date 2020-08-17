package com.nirshal.util.excel.renderers;

import com.garmin.fit.Sport;

public enum UnitsType {
    RUNNING(Sport.getStringFromValue(Sport.RUNNING),1000.0/360000, DistanceUnit.KM, PaceUnit.SI_RUNNING, SpeedUnit.SI_RUNNING),
    WALKING(Sport.getStringFromValue(Sport.WALKING),1000.0/360000, DistanceUnit.KM, PaceUnit.SI_RUNNING, SpeedUnit.SI_RUNNING),
    SWIMMING(Sport.getStringFromValue(Sport.SWIMMING),0.0,DistanceUnit.KM, PaceUnit.SI_SWIMMING, SpeedUnit.SI_SWIMMING),
    CYCLING(Sport.getStringFromValue(Sport.CYCLING),0.0,DistanceUnit.KM,PaceUnit.SI_CYCLING, SpeedUnit.SI_CYCLING),
    WORKOUT(Sport.getStringFromValue(Sport.TRAINING),0.0,DistanceUnit.KM,PaceUnit.SI_RUNNING, SpeedUnit.SI_RUNNING),
    UNKNOWN("UNRECOGNIZED ACTIVITY",0.0,DistanceUnit.KM,PaceUnit.SI_RUNNING, SpeedUnit.SI_RUNNING),
    EMPTY("RIPOSO",0.0,DistanceUnit.KM,PaceUnit.SI_RUNNING, SpeedUnit.SI_RUNNING);

    private final String defaultDescription;
    private final double recoverySpeed;
    private final DistanceUnit distanceUnit;
    private final PaceUnit paceUnit;
    private final SpeedUnit speedUnit;

    UnitsType(String defaultDescription,
              double recoverySpeed,
              DistanceUnit distanceUnit,
              PaceUnit paceUnit,
              SpeedUnit speedUnit) {
        this.defaultDescription = defaultDescription;
        this.recoverySpeed = recoverySpeed;
        this.distanceUnit = distanceUnit;
        this.paceUnit = paceUnit;
        this.speedUnit = speedUnit;
    }

    public String getDefaultDescription() {
        return defaultDescription;
    }

    public double getRecoverySpeed() {
        return recoverySpeed;
    }

    public DistanceUnit getDistanceUnit() {
        return distanceUnit;
    }

    public PaceUnit getPaceUnit() {
        return paceUnit;
    }

    public SpeedUnit getSpeedUnit() {
        return speedUnit;
    }
}
