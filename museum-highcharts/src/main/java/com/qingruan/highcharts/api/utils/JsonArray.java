package com.qingruan.highcharts.api.utils;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class JsonArray<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;

    public E getItem(int index) {
        return get(index);
    }

    public Integer getLength() {
        return size();
    }

    public int length() {
        return size();
    }

    public JsonArray<E> pushElement(E value) {
        pushItem(value);
        return this;
    }

    public void pushItem(E value) {
        add(value);
    }

    public JsonArray<E> setElement(int index, E value) {
        setItem(index, value);
        return this;
    }

    public void setItem(int index, E value) {
        if (size() > index) {
            set(index, value);
        }
        else if (size() == index) {
            add(index, value);
        }
        else {
            while (size() < index) {
                add(null);
            }
            add(index, value);
        }
    }
}
