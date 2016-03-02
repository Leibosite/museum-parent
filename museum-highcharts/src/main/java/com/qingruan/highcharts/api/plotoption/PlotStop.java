package com.qingruan.highcharts.api.plotoption;

import java.awt.Color;

public class PlotStop {

    private final int pos;

    private final Color color;

    PlotStop(int pos, Color color) {
        super();
        this.pos = pos;
        this.color = color;
    }

    public int getPos() {
        return pos;
    }

    public Color getColor() {
        return color;
    }

}
