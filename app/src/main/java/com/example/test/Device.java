package com.example.test;

import java.io.Serializable;

public class Device implements Serializable {
    private String username;
    private String io_key;
    private String displayname;

    public Device(String username, String key, String displayname){
        this.username = username;
        this.io_key = key;
        this.displayname = displayname;
    }
    void setDisplayName(String name){
        this.displayname = name;
    }
    @Override
    public String toString() {
        return this.displayname;
    }
    String getKey(){
        return this.io_key;
    }
    String getDisplayname(){
        return this.displayname;
    }

}
