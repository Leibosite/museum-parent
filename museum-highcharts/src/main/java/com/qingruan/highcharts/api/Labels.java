package com.qingruan.highcharts.api;

import javax.xml.bind.annotation.*;

import com.qingruan.highcharts.api.base.BaseObject;
import com.qingruan.highcharts.api.label.LabelsItems;
import com.qingruan.highcharts.api.utils.JsonArray;


@XmlAccessorType(XmlAccessType.NONE)
@XmlType(namespace = "chart-options")
public class Labels extends BaseObject {

    @XmlTransient
    private JsonArray<LabelsItems> items;

    public Labels() {
    }

    @XmlTransient
    public JsonArray<LabelsItems> getItems() {
        if (items == null) {
            items = new JsonArray<LabelsItems>();
        }

        return items;
    }

}
