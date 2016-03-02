package com.qingruan.highcharts.api.serializer;

import java.lang.reflect.Type;


import com.google.gson.*;
import com.qingruan.highcharts.api.serie.SeriesCenter;

public class CenterSerializer implements JsonSerializer<SeriesCenter> {

    @Override
    public JsonElement serialize(SeriesCenter center, Type arg1, JsonSerializationContext arg2) {
        if (center.getX() == null || center.getY() == null) {
            return new JsonPrimitive("");
        }
        JsonArray r = new JsonArray();
        r.add(new JsonPrimitive(center.getX()));
        r.add(new JsonPrimitive(center.getY()));
        return r;
    }

}
