package com.example.test;

public class Humid {
    private int id;
    private int humid;


    public Humid(int id, int humid) {
        this.id = id;
        this.humid = humid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHumid() {
        return humid;
    }

    public void setHumid(int humid) {
        this.humid = humid;
    }
}
