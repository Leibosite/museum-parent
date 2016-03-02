package com.qingruan.highcharts.api;

import com.qingruan.highcharts.api.base.BaseObject;

public class Credits extends BaseObject {

    private Boolean enabled;

    public boolean istEnabled() {
        return enabled;
    }

    public Credits setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

}
