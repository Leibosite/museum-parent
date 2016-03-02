package com.qingruan.highcharts.api.utils;

@SuppressWarnings("serial")
public class ArrayString extends JsonArray<String> {

    public String join() {
        return join(",");
    }

    public String join(String separator) {
        StringBuffer sb = new StringBuffer();
        int size = length();

        if (size < 1) {
            return sb.toString();
        }

        sb.append(getItem(0));
        for (int i = 1; i < size; i++) {
            sb.append(separator);
            sb.append(getItem(i));
        }

        return sb.toString();
    }

    public ArrayString pushString(String value) {
        pushItem(value);
        return this;
    }

    public void setLength(int newLength) {
    }

    public ArrayString setString(int index, String value) {
        setItem(index, value);
        return this;
    }

    public String shiftItem() {
        if (length() < 1) {
            return null;
        }

        return remove(0);
    }

    public void unshiftItem(String value) {
        add(0, value);
    }

}
