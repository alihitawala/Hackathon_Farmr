package com.climate.farmr.domain;

import java.io.Serializable;

/**
 * Created by aliHitawala on 2/20/16.
 */
public class Point implements Serializable{
    private double lat;
    private double log;

    public Point(double log, double lat) {
        this.lat = lat;
        this.log = log;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }
}
