package com.qingruan.highcharts.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.qingruan.highcharts.api.base.BaseObject;


@XmlAccessorType(XmlAccessType.NONE)
public class Legend extends BaseObject {

    private Boolean reversed;

    private Boolean enabled;

    private String align;

    private String layout;

    private String verticalAlign;

    @XmlElement
    private Integer x;

    @XmlElement
    private Integer y;

    public String getAlign() {
        return align;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public String getLayout() {
        return layout;
    }

    public String getVerticalAlign() {
        return verticalAlign;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isReversed() {
        return reversed;
    }

    @XmlElement
    public Legend setAlign(String align) {
        this.align = align;
        return this;
    }

    public Legend setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @XmlElement
    public Legend setLayout(String layout) {
        this.layout = layout;
        return this;
    }

    public Legend setReversed(boolean reversed) {
        this.reversed = reversed;
        return this;
    }

    @XmlElement
    public Legend setVerticalAlign(String verticalAlign) {
        this.verticalAlign = verticalAlign;
        return this;
    }

    public Legend setX(int x) {
        this.x = x;
        return this;
    }

    public Legend setY(int y) {
        this.y = y;
        return this;
    }

}
