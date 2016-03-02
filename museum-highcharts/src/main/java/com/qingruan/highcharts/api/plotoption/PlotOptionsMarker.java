package com.qingruan.highcharts.api.plotoption;

import com.qingruan.highcharts.api.base.BaseObject;

public class PlotOptionsMarker extends BaseObject {

    private String symbol;

    private boolean enabled;

    private PlotMarkerStates states;

    public PlotOptionsMarker() {
    }

    public PlotMarkerStates getStates() {
        if (states == null) {
            states = new PlotMarkerStates();
        }
        return states;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public PlotOptionsMarker setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public PlotOptionsMarker setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }
}