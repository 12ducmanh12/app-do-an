package com.example.test;

public class Temp {
    private int id;
    private double temp;

    public Temp(int id, double temp) {
        this.id = id;
        this.temp = temp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
