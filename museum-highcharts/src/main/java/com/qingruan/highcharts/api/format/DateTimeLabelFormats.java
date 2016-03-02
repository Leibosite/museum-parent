package com.qingruan.highcharts.api.format;

import java.util.HashMap;
import java.util.Map;

import com.qingruan.highcharts.api.base.BaseObject;


public class DateTimeLabelFormats extends BaseObject {

    public enum TimeUnit {
        SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, YEAR;
    }

    private Map<TimeUnit, String> formats;

    public DateTimeLabelFormats() {
        formats = null;
    }

    public String getFormat(TimeUnit unit) {
        return formats != null ? formats.get(unit) : null;
    }

    public Map<TimeUnit, String> getFormats() {
        return formats;
    }

    public DateTimeLabelFormats set(TimeUnit unit, String format) {
        if (formats == null) {
            formats = new HashMap<TimeUnit, String>();
        }
        formats.put(unit, format);
        return this;
    }

}
