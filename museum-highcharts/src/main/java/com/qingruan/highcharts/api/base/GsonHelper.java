package com.qingruan.highcharts.api.base;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.qingruan.highcharts.api.format.DateTimeLabelFormats;
import com.qingruan.highcharts.api.serializer.DateTimeLabelFormatsSerializer;
import com.qingruan.highcharts.api.serializer.StyleSerializer;

public final class GsonHelper {

    private static final String DATE_FORMAT = "yyyyMMdd";

    private static final String USER_OBJECT = "userObject";

    private static final GsonHelper INSTANCE = new GsonHelper();

    static String toJson(Object object) {
        return GsonHelper.INSTANCE.gsonBuilder.create().toJson(object);
    }

    private final GsonBuilder gsonBuilder;

    private GsonHelper() {
        gsonBuilder = new GsonBuilder()
        .registerTypeAdapter(DateTimeLabelFormats.class, new DateTimeLabelFormatsSerializer()) //
        .registerTypeAdapter(Style.class, new StyleSerializer())//
        .setDateFormat(GsonHelper.DATE_FORMAT)//
        .setExclusionStrategies(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipClass(Class<?> arg0) {
                return false;
            }

            @Override
            public boolean shouldSkipField(FieldAttributes attributes) {
                return attributes.getName().equals(GsonHelper.USER_OBJECT);
            }

        });
    }
}
