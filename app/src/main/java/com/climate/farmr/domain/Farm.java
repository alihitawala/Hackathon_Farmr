package com.climate.farmr.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aliHitawala on 2/20/16.
 */
public class Farm implements Serializable{
    private String timezone;
    private String farmNumber;
    private String tractNumber;
    private String acres;
    private String county;
    private String state;
    private Point centroid;
    private String defaultSoilType;
    private List<Soil> soils = new ArrayList<>();
    private List<Point> coordinates = new ArrayList<>();
    private String geometryType;
    private double value;
    private double distance;
    private double pop;

    public double getPop() {
        return pop;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private Map<String, Double> cropToValue = new HashMap<>();

    public Map<String, Double> getCropValueMap() {
        return cropToValue;
    }
    public void putCropValue(String crop, double value) {
        cropToValue.put(crop, value);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getFarmNumber() {
        return farmNumber;
    }

    public void setFarmNumber(String farmNumber) {
        this.farmNumber = farmNumber;
    }

    public String getTractNumber() {
        return tractNumber;
    }

    public void setTractNumber(String tractNumber) {
        this.tractNumber = tractNumber;
    }

    public String getAcres() {
        return acres;
    }

    public void setAcres(String acres) {
        this.acres = acres;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    public String getDefaultSoilType() {
        return defaultSoilType;
    }

    public void setDefaultSoilType(String defaultSoilType) {
        this.defaultSoilType = defaultSoilType;
    }

    public List<Soil> getSoils() {
        return soils;
    }

    public void setSoils(List<Soil> soils) {
        this.soils = soils;
    }

    public List<Point> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Point> coordinates) {
        this.coordinates = coordinates;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }
}
