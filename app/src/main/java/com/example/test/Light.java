package com.example.test;

public class Light {
    private int id;
    private double light;

    public Light(int id, double light) {
        this.id = id;
        this.light = light;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLight() {
        return this.light;
    }

    public void setLight(double light) {
        this.light = light;
    }
}
