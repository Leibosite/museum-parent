package com.qingruan.highcharts.api;

import javax.xml.bind.annotation.*;

import com.qingruan.highcharts.api.axis.AxisLabels;
import com.qingruan.highcharts.api.axis.AxisPlotLines;
import com.qingruan.highcharts.api.base.BaseObject;
import com.qingruan.highcharts.api.format.DateTimeLabelFormats;
import com.qingruan.highcharts.api.utils.ArrayString;
import com.qingruan.highcharts.api.utils.JsonArray;

@XmlAccessorType(XmlAccessType.NONE)
public class Axis extends BaseObject {
	@XmlElement
	private String tickmarkPlacement;
	@XmlElement
	private Integer lineWidth;
	@XmlElement
	private String gridLineInterpolation;

	@XmlElement
	private Integer maxZoom;

	@XmlElement
	private Boolean startOnTick;

	@XmlElement
	private Double tickInterval;

	@XmlElement
	private Boolean showFirstLabel;

	@XmlElement
	private Double min;

	@XmlElement
	private Double max;

	@XmlElement
	private String type;

	@XmlElement(type = AxisLabels.class)
	private AxisLabels labels;

	private DateTimeLabelFormats dateTimeLabelFormats;

	@XmlTransient
	private ArrayString categories;

	private JsonArray<AxisPlotLines> plotLines;

	private Title title;

	public Axis() {
		labels = null;
		dateTimeLabelFormats = null;
		categories = null;
		title = null;
	}

	@XmlTransient
	public ArrayString getCategories() {
		if (categories == null) {
			categories = new ArrayString();
		}
		return categories;
	}

	public int getCategoriesLength() {
		return categories != null ? categories.size() : 0;
	}

	public DateTimeLabelFormats getDateTimeLabelFormats() {
		if (dateTimeLabelFormats == null) {
			dateTimeLabelFormats = new DateTimeLabelFormats();
		}
		return dateTimeLabelFormats;
	}

	public AxisLabels getLabels() {
		if (labels == null) {
			labels = new AxisLabels();
		}
		return labels;
	}

	public double getMax() {
		return max;
	}

	public int getMaxZoom() {
		return maxZoom;
	}

	public double getMin() {
		return min;
	}

	public JsonArray<AxisPlotLines> getPlotLines() {
		return plotLines;
	}

	public double getTickInterval() {
		return tickInterval;
	}

	public Title getTitle() {
		if (title == null) {
			title = new Title();
		}
		return title;
	}

	public String getType() {
		return type;
	}

	public boolean isShowFirstLabel() {
		return showFirstLabel;
	}

	public boolean isStartOnTick() {
		return startOnTick;
	}

	public Axis setMax(double max) {
		this.max = max;
		return this;
	}

	public Axis setMaxZoom(int maxZoom) {
		this.maxZoom = maxZoom;
		return this;
	}

	public Axis setMin(double min) {
		this.min = min;
		return this;
	}

	public Axis setPlotLines(JsonArray<AxisPlotLines> plotLines) {
		this.plotLines = plotLines;
		return this;
	}

	public Axis setShowFirstLabel(boolean showFirstLabel) {
		this.showFirstLabel = showFirstLabel;
		return this;
	}

	public Axis setStartOnTick(boolean startOnTick) {
		this.startOnTick = startOnTick;
		return this;
	}

	public Axis setTickInterval(double tickInterval) {
		this.tickInterval = tickInterval;
		return this;
	}

	public Axis setType(String type) {
		this.type = type;
		return this;
	}

	public String getTickmarkPlacement() {
		return tickmarkPlacement;
	}

	public void setTickmarkPlacement(String tickmarkPlacement) {
		this.tickmarkPlacement = tickmarkPlacement;
	}

	public Integer getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(Integer lineWidth) {
		this.lineWidth = lineWidth;
	}

	public String getGridLineInterpolation() {
		return gridLineInterpolation;
	}

	public void setGridLineInterpolation(String gridLineInterpolation) {
		this.gridLineInterpolation = gridLineInterpolation;
	}

}
