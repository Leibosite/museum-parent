package com.qingruan.highcharts.api.plotoption;

import javax.xml.bind.annotation.*;

import com.qingruan.highcharts.api.base.BaseObject;


@XmlType(namespace = "plotoptions")
@XmlAccessorType(XmlAccessType.NONE)
public class PlotOptionsSeries extends BaseObject {

    @XmlElement(type = PlotOptionsStates.class)
    private PlotOptionsStates states;

    private PlotOptionsDataLabels dataLabels;

    private String stacking;

    private PlotOptionsMarker marker;

    @XmlElement
    private Boolean allowPointSelect;

    @XmlElement
    private Boolean shadow;

    @XmlElement
    private Integer lineWidth;

    @XmlElement
    private Double fillOpacity;

    private Integer borderWidth;

    @XmlElement
    private PlotColor fillColor;

    public int getBorderWidth() {
        return borderWidth;
    }

    public PlotOptionsDataLabels getDataLabels() {
        if (dataLabels == null) {
            dataLabels = new PlotOptionsDataLabels();
        }
        return dataLabels;
    }

    public double getFillOpacity() {
        return fillOpacity;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public PlotOptionsMarker getMarker() {
        if (marker == null) {
            marker = new PlotOptionsMarker();
        }
        return marker;
    }

    public String getStacking() {
        return stacking;
    }

    public PlotOptionsStates getStates() {
        if (states == null) {
            states = new PlotOptionsStates();
        }
        return states;
    }

    public boolean isAllowPointSelect() {
        return allowPointSelect;
    }

    public boolean isShadow() {
        return shadow;
    }

    public PlotOptionsSeries setAllowPointSelect(boolean allowPointSelect) {
        this.allowPointSelect = allowPointSelect;
        return this;
    }

    public PlotOptionsSeries setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public PlotOptionsSeries setFillOpacity(double fillOpacity) {
        this.fillOpacity = fillOpacity;
        return this;
    }

    public PlotOptionsSeries setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public PlotOptionsSeries setShadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    @XmlElement
    public PlotOptionsSeries setStacking(String stacking) {
        this.stacking = stacking;
        return this;
    }

    public PlotOptionsSeries setStates(PlotOptionsStates states) {
        this.states = states;
        return this;
    }

    public PlotColor getFillColor() {
        if (fillColor == null) {
            fillColor = new PlotColor();
        }
        return fillColor;
    }

}