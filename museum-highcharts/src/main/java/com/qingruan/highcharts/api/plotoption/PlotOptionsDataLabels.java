package com.qingruan.highcharts.api.plotoption;

import com.qingruan.highcharts.api.base.BaseObject;

public class PlotOptionsDataLabels extends BaseObject {

    private String color;

    private boolean enabled;

    private Object formatter;

    private Integer x, y, distance;

    private String align = "center";

    private double rotation;

    public String getAlign() {
        return align;
    }

    public String getColor() {
        return color;
    }

    public int getDistance() {
        return distance;
    }

    public Object getFormatter() {
        return formatter;
    }

    public double getRotation() {
        return rotation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public PlotOptionsDataLabels setAlign(String align) {
        this.align = align;
        return this;
    }

    public PlotOptionsDataLabels setColor(String color) {
        this.color = color;
        return this;
    }

    public PlotOptionsDataLabels setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public PlotOptionsDataLabels setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public PlotOptionsDataLabels setFormatter(Object formatter) {
        this.formatter = formatter;
        return this;
    }

    public PlotOptionsDataLabels setRotation(double rotation) {
        this.rotation = rotation;
        return this;
    }

    public PlotOptionsDataLabels setX(int x) {
        this.x = x;
        return this;
    }

    public PlotOptionsDataLabels setY(int y) {
        this.y = y;
        return this;
    }

}