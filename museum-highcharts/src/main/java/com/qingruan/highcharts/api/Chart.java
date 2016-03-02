package com.qingruan.highcharts.api;

import com.qingruan.highcharts.api.base.BaseObject;
import com.qingruan.highcharts.api.shared.SeriesType;

public class Chart extends BaseObject {

    private String zoomType;

    private String backgroundColor;

    private Integer marginRight;

    private Integer marginBottom;

    private Integer width;

    private Integer height;

    private String defaultSeriesType;

    private Integer marginLeft;

    private Integer marginTop;
    
    private String renderTo;
    //Polar为true，则可将图转成极地图,但需引入highcharts-more.js. 默认是： false
    private Boolean polar;
    //指定绘制区所要绘制的图的类型，例如：type=bar为柱图，type=line为线图
    private String type;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getDefaultSeriesType() {
        return defaultSeriesType;
    }

    public int getHeight() {
        return height;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public int getWidth() {
        return width;
    }

    public String getZoomType() {
        return zoomType;
    }

    public Chart setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public Chart setDefaultSeriesType(SeriesType type) {
        defaultSeriesType = type.name().toLowerCase();
        return this;
    }

    public Chart setHeight(int height) {
        this.height = height;
        return this;
    }

    public Chart setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
        return this;
    }

    public Chart setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
        return this;
    }

    public Chart setMarginRight(int marginRight) {
        this.marginRight = marginRight;
        return this;
    }

    public Chart setMarginTop(int marginTop) {
        this.marginTop = marginTop;
        return this;
    }

    public Chart setWidth(int width) {
        this.width = width;
        return this;
    }

    public Chart setZoomType(String zoomType) {
        this.zoomType = zoomType;
        return this;
    }

	public String getRenderTo() {
		return renderTo;
	}

	public void setRenderTo(String renderTo) {
		this.renderTo = renderTo;
	}

	public Boolean getPolar() {
		return polar;
	}

	public void setPolar(Boolean polar) {
		this.polar = polar;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
