package com.qingruan.highcharts.api.plotoption;

import javax.xml.bind.annotation.XmlElement;

import com.qingruan.highcharts.api.base.BaseObject;


public class PlotOptionsStates extends BaseObject {

    @XmlElement(type = PlotStatesSelect.class)
    private PlotStatesSelect select;

    public PlotStatesSelect getSelect() {
        if (select == null) {
            select = new PlotStatesSelect();
        }
        return select;
    }
}