package com.qingruan.highcharts.api.shared;

public interface SeriesTypeVisitor<I, O> {

    O visitArea(SeriesType type, I in);

    O visitAreaspline(SeriesType type, I in);

    O visitBar(SeriesType type, I in);

    O visitColumn(SeriesType type, I in);

    O visitLine(SeriesType type, I in);

    O visitPie(SeriesType type, I in);

    O visitScatter(SeriesType type, I in);

    O visitSpline(SeriesType type, I in);

    O visitTable(SeriesType type, I in);

}
