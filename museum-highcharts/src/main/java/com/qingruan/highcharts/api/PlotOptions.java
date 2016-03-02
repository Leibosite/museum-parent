package com.qingruan.highcharts.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.qingruan.highcharts.api.base.BaseObject;
import com.qingruan.highcharts.api.plotoption.PlotOptionsSeries;


@XmlAccessorType(XmlAccessType.NONE)
public class PlotOptions extends BaseObject {

    @XmlElement
    private PlotOptionsSeries area;

    private PlotOptionsSeries areaspline;

    private PlotOptionsSeries line;

    private PlotOptionsSeries pie;

    private PlotOptionsSeries series;

    private PlotOptionsSeries spline;

    private PlotOptionsSeries column;

    private PlotOptionsSeries bar;

    public PlotOptions() {
        super();
    }

    public PlotOptionsSeries getArea() {
        if (area == null) {
            area = new PlotOptionsSeries();
        }
        return area;
    }

    public PlotOptionsSeries getAreaspline() {
        if (areaspline == null) {
            areaspline = new PlotOptionsSeries();
        }
        return areaspline;
    }

    public PlotOptionsSeries getBar() {
        if (bar == null) {
            bar = new PlotOptionsSeries();
        }
        return bar;
    }

    public PlotOptionsSeries getColumn() {
        if (column == null) {
            column = new PlotOptionsSeries();
        }
        return column;
    }

    public PlotOptionsSeries getLine() {
        if (line == null) {
            line = new PlotOptionsSeries();
        }
        return line;
    }

    public PlotOptionsSeries getPie() {
        if (pie == null) {
            pie = new PlotOptionsSeries();
        }
        return pie;
    }

    public PlotOptionsSeries getSeries() {
        if (series == null) {
            series = new PlotOptionsSeries();
        }
        return series;
    }

    public PlotOptionsSeries getSpline() {
        if (spline == null) {
            spline = new PlotOptionsSeries();
        }
        return spline;
    }
}
