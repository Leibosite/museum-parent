package com.qingruan.highcharts.api.plotoption;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class PlotColor {

    private PlotGradient linearGradient;

    private List<Object[]> stops;

    public PlotGradient getLinearGradient() {
        if (linearGradient == null) {
            linearGradient = new PlotGradient();
        }
        return linearGradient;
    }

    public void addPlotStop(int stop, Color color) {
        if (stops == null) {
            stops = new LinkedList<Object[]>();
        }
        Object[] objs = new Object[2];
        objs[0] = stops.size();
        objs[1] = Integer.toHexString(color.getRGB());
        ;
        stops.add(objs);
    }

}
