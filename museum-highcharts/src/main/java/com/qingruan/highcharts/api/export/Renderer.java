package com.qingruan.highcharts.api.export;

import java.io.OutputStream;

public interface Renderer<T> {

    public static abstract class PojoRenderer<T> implements Renderer<T> {

        private T options, globalOptions;

        private OutputStream output;

        protected T getChartOptions() {
            return options;
        }

        protected T getGlobalOptions() {
            return globalOptions;
        }

        protected OutputStream getOutputStream() {
            return output;
        }

        @Override
        public Renderer<T> setChartOptions(T options) {
            this.options = options;
            return this;
        }

        @Override
        public Renderer<T> setGlobalOptions(T options) {
            this.globalOptions = options;
            return this;
        }

        @Override
        public Renderer<T> setOutputStream(OutputStream output) {
            this.output = output;
            return this;
        }

    }

    void render();

    Renderer<T> setChartOptions(T options);

    Renderer<T> setGlobalOptions(T options);

    Renderer<T> setOutputStream(OutputStream outputStream);

}