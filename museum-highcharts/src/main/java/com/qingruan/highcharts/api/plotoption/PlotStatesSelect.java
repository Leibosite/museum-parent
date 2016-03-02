package com.qingruan.highcharts.api.plotoption;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.qingruan.highcharts.api.base.BaseObject;


@XmlAccessorType(XmlAccessType.NONE)
public class PlotStatesSelect extends BaseObject {

    private String color;

    private String borderColor;

    @XmlElement
    private Integer borderWidth;

    public String getBorderColor() {
        return borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public String getColor() {
        return color;
    }

    public PlotStatesSelect setBorderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public PlotStatesSelect setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public PlotStatesSelect setColor(String color) {
        this.color = color;
        return this;
    }

}