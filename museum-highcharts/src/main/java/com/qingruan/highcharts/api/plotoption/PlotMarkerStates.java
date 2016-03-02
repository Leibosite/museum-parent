package com.qingruan.highcharts.api.plotoption;

import com.qingruan.highcharts.api.base.BaseObject;

public class PlotMarkerStates extends BaseObject {

    private final PlotMarkerSelect select;

    private final PlotMarkerHover hover;

    public PlotMarkerStates() {
        select = new PlotMarkerSelect();
        hover = new PlotMarkerHover();
    }

    public PlotMarkerHover getHover() {
        return hover;
    }

    public PlotMarkerSelect getSelect() {
        return select;
    }
}