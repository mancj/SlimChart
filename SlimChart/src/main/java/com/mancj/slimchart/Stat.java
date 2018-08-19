package com.mancj.slimchart;

public class Stat {

    Float s_value;
    Integer s_color;

    public Stat() {
    }

    public Stat(Float value, Integer color) {
        s_value = value;
        s_color = color;
    }

    public Float getValue() {
        return s_value;
    }

    public void setValue(Float value) {
        s_value = value;
    }

    public Integer getColor() {
        return s_color;
    }

    public void setColor(Integer color) {
        s_color = color;
    }
}
