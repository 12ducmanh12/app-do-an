package com.example.test;

public class History {

    private int id;
    private String time;
    private String date;
    private double temp;
    private int humid;


    public History(int id, String time, String date, double temp, int humid) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.temp = temp;
        this.humid = humid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getHumid() {
        return humid;
    }

    public void setHumid(int humid) {
        this.humid = humid;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", temp=" + temp +
                ", humid=" + humid +
                '}';
    }
}
