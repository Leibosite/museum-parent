package com.qingruan.highcharts.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.qingruan.highcharts.api.base.BaseObject;
import com.qingruan.highcharts.api.base.Style;


@XmlAccessorType(XmlAccessType.NONE)
public class Title extends BaseObject {

    @XmlElement
    private String text;

    private Style style;

    @XmlElement
    private Integer margin;
    //相对于水平对齐的偏移量，取值范围为：图表左边距到图表右边距，可以是负值，单位px。
    //默认是： 0.
    private Integer x;
    //相对于垂直对齐的偏移量，取值范围：图表的上边距到图表的下边距，可以是负值，单位是px。 默认： 取决于字体的大小
    private Integer y;

    public Title() {
        style = null;
        text = "";
    }

    public int getMargin() {
        return margin;
    }

    public Style getStyle() {
        if (style == null) {
            style = new Style();
        }
        return style;
    }

    public String getText() {
        return text;
    }

    public Title setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public Title setText(String text) {
        this.text = text;
        return this;
    }

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

}
