package com.example.api;

import java.util.Date;

public class Task {
    public String itemName;
    public String location;
    public String instruction;
    public String date;
    public String status = "Unasigned";
    public boolean isEmergency = false;
    public String repairerID="";
    public String interval;

    public Task(){
    }

    public Task(String itemName, String location, String instruction, String date, String status, String interval) {
        this.itemName = itemName;
        this.location = location;
        this.instruction = instruction;
        this.date = date;
        this.status = status;
        this.interval = interval;
    }
    public Task(String itemName, String location, String instruction, String date, String interval, boolean isEmergency) {
        this.itemName = itemName;
        this.location = location;
        this.instruction = instruction;
        this.date = date;
        this.interval = interval;
        this.isEmergency = isEmergency;

    }
}