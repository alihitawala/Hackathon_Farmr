package com.climate.farmr.domain;

/**
 * Created by aliHitawala on 2/20/16.
 */
public class Field {
    private String name;
    private String farmId;
    private Point centroid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }
}
