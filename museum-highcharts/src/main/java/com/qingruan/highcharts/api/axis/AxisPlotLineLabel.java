package com.qingruan.highcharts.api.axis;

import com.qingruan.highcharts.api.base.BaseObject;
import com.qingruan.highcharts.api.base.Style;

public class AxisPlotLineLabel extends BaseObject {

    private String align;

    private String verticalAlign;

    private double rotation;

    private String text;

    private String textAlign;

    private double x;

    private double y;

    private Style style;

    public AxisPlotLineLabel() {
        style = new Style();
    }

    public String getAlign() {
        return align;
    }

    public double getRotation() {
        return rotation;
    }

    public Style getStyle() {
        return style;
    }

    public String getText() {
        return text;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public String getVerticalAlign() {
        return verticalAlign;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public AxisPlotLineLabel setAlign(String align) {
        this.align = align;
        return this;
    }

    public AxisPlotLineLabel setRotation(double rotation) {
        this.rotation = rotation;
        return this;
    }

    public AxisPlotLineLabel setStyle(Style style) {
        this.style = style;
        return this;
    }

    public AxisPlotLineLabel setText(String text) {
        this.text = text;
        return this;
    }

    public AxisPlotLineLabel setTextAlign(String textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    public AxisPlotLineLabel setVerticalAlign(String verticalAlign) {
        this.verticalAlign = verticalAlign;
        return this;
    }

    public AxisPlotLineLabel setX(double x) {
        this.x = x;
        return this;
    }

    public AxisPlotLineLabel setY(double y) {
        this.y = y;
        return this;
    }

}