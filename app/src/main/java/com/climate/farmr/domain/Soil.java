package com.climate.farmr.domain;

import java.io.Serializable;

/**
 * Created by aliHitawala on 2/20/16.
 */
public class Soil implements Serializable {
    private String name;
    private String quatity;
    private String unit;
    private String fieldProportion;
    private String hydrogroup;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuatity() {
        return quatity;
    }

    public void setQuatity(String quatity) {
        this.quatity = quatity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFieldProportion() {
        return fieldProportion;
    }

    public void setFieldProportion(String fieldProportion) {
        this.fieldProportion = fieldProportion;
    }

    public String getHydrogroup() {
        return hydrogroup;
    }

    public void setHydrogroup(String hydrogroup) {
        this.hydrogroup = hydrogroup;
    }
}
