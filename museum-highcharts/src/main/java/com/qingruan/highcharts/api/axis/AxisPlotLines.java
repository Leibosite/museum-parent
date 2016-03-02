package com.qingruan.highcharts.api.axis;

import com.qingruan.highcharts.api.base.BaseObject;

public class AxisPlotLines extends BaseObject {

    private int zindex;

    private int width;

    private double value;

    private String id;

    private String color;

    private String dashStyle;

    private AxisPlotLineLabel label;

    public AxisPlotLines() {
        label = new AxisPlotLineLabel();
    }

    public String getColor() {
        return color;
    }

    public String getDashStyle() {
        return dashStyle;
    }

    public String getId() {
        return id;
    }

    public AxisPlotLineLabel getLabel() {
        return label;
    }

    public double getValue() {
        return value;
    }

    public int getWidth() {
        return width;
    }

    public int getZIndex() {
        return zindex;
    }

    public AxisPlotLines setColor(String color) {
        this.color = color;
        return this;
    }

    public AxisPlotLines setDashStyle(String dashStyle) {
        this.dashStyle = dashStyle;
        return this;
    }

    public AxisPlotLines setId(String id) {
        this.id = id;
        return this;
    }

    public AxisPlotLines setLabel(AxisPlotLineLabel label) {
        this.label = label;
        return this;
    }

    public AxisPlotLines setValue(double value) {
        this.value = value;
        return this;
    }

    public AxisPlotLines setWidth(int width) {
        this.width = width;
        return this;
    }

    public AxisPlotLines setZIndex(int zindex) {
        this.zindex = zindex;
        return this;
    }
}