package com.qingruan.highcharts;


import org.junit.Assert;
import org.junit.Test;

import com.qingruan.highcharts.api.ChartOptions;
import com.qingruan.highcharts.api.Point;
import com.qingruan.highcharts.api.Series;
import com.qingruan.highcharts.api.shared.SeriesType;
import com.qingruan.highcharts.api.utils.JsonArray;

public class TestColumnBasic {

    private static final String columnBasicJson = "{\"chart\":" +
            "{\"width\":800,\"height\":400,\"defaultSeriesType\":\"column\",\"marginLeft\":70,\"marginTop\":80}," +
            "\"legend\":{\"align\":\"left\",\"layout\":\"vertical\",\"verticalAlign\":\"top\",\"x\":100,\"y\":70}," +
            "\"plotOptions\":{\"column\":{\"borderWidth\":0}}," +
            "\"series\":[{\"data\":[{\"y\":49.9},{\"y\":71.5},{\"y\":106.4},{\"y\":129.2},{\"y\":144.0},{\"y\":176.0},{\"y\":135.6},{\"y\":148.5},{\"y\":216.4},{\"y\":194.1},{\"y\":95.6},{\"y\":54.4}],\"name\":\"Tokyo\"}," +
            "{\"data\":[{\"y\":83.6},{\"y\":78.8},{\"y\":98.5},{\"y\":93.4},{\"y\":106.0},{\"y\":84.5},{\"y\":105.0},{\"y\":104.3},{\"y\":91.2},{\"y\":83.5},{\"y\":106.6},{\"y\":92.3}],\"name\":\"New York\"}," +
            "{\"data\":[{\"y\":48.9},{\"y\":38.8},{\"y\":39.3},{\"y\":41.4},{\"y\":47.0},{\"y\":48.3},{\"y\":59.0},{\"y\":59.6},{\"y\":52.4},{\"y\":65.2},{\"y\":59.3},{\"y\":51.2}],\"name\":\"London\"}," +
            "{\"data\":[{\"y\":42.4},{\"y\":33.2},{\"y\":34.5},{\"y\":39.7},{\"y\":52.6},{\"y\":75.5},{\"y\":57.4},{\"y\":60.4},{\"y\":47.6},{\"y\":39.1},{\"y\":46.8},{\"y\":51.1}],\"name\":\"Berlin\"}]," +
            "\"title\":{\"text\":\"Monthly Average Rainfall\"},\"subtitle\":{\"text\":\"Source: WorldClimate.com\"},\"xAxis\":{\"categories\":[\"Jan\",\"Feb\",\"Mar\",\"Apr\",\"May\",\"Jun\",\"Jul\",\"Aug\",\"Sep\",\"Oct\",\"Nov\",\"Dec\"]},\"yAxis\":" +
            "{\"min\":0.0,\"title\":{\"text\":\"Rainfall (mm)\"}}}";

    @Test
    public void createColumnBasic() {
        // http://highcharts.com/demo/column-basic
        ChartOptions chartOptions = new ChartOptions();

        chartOptions.getChart().setDefaultSeriesType(SeriesType.COLUMN).setWidth(800).setHeight(400).setMarginLeft(70)
                .setMarginTop(80);

        // titles
        chartOptions.getTitle().setText("Monthly Average Rainfall");
        chartOptions.getSubtitle().setText("Source: WorldClimate.com");

        // xAxis
        chartOptions.getXAxis().getCategories().pushString("Jan").pushString("Feb").pushString("Mar").pushString("Apr")
                .pushString("May").pushString("Jun").pushString("Jul").pushString("Aug").pushString("Sep")
                .pushString("Oct").pushString("Nov").pushString("Dec");
        // yAxis
        chartOptions.getYAxis().setMin(0).getTitle().setText("Rainfall (mm)");

        // Legend
        chartOptions.getLegend().setLayout("vertical").setAlign("left").setVerticalAlign("top").setX(100).setY(70);

        // PlotOptions
        chartOptions.getPlotOptions().getColumn().setBorderWidth(0);

        // Several series
        addSeries(chartOptions, "Tokyo", new double[] { 49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4,
                194.1, 95.6, 54.4 });

        addSeries(chartOptions, "New York", new double[] { 83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2,
                83.5, 106.6, 92.3 });

        addSeries(chartOptions, "London", new double[] { 48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2,
                59.3, 51.2 });

        addSeries(chartOptions, "Berlin", new double[] { 42.4, 33.2, 34.5, 39.7, 52.6, 75.5, 57.4, 60.4, 47.6, 39.1,
                46.8, 51.1 });

        String json = chartOptions.toJson();
        System.out.println("Here");
        System.out.println(json);

        Assert.assertEquals("Expected column basic json", TestColumnBasic.columnBasicJson, json);
    }

    @Test
    public void testWrongMargins() {
        ChartOptions chartOptions = new ChartOptions();

        chartOptions.getChart().setDefaultSeriesType(SeriesType.COLUMN); // do not set any margins!
        String json = chartOptions.toJson();

        Assert.assertEquals("There should be no margins in the json", "{\"chart\":{\"defaultSeriesType\":\"column\"}}",
                json);
    }

    private void addSeries(ChartOptions chartOptions, String seriesName, double[] datas) {

        Series newSeries = new Series().setName(seriesName);
        final JsonArray<Point> seriesDatas = newSeries.getData();
        for (double d : datas) {
            seriesDatas.pushElement(new Point().setY(d));
        }
        chartOptions.getSeries().pushElement(newSeries);
    }

}
