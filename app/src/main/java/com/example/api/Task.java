package com.example.api;

public class Task {
    public String itemName;
    public String location;
    public String instruction;
    public String date;
    public String status = "Unassigned";
    public boolean isEmergency = false;
    public String repairerID="";
    public String interval;
    public int norma = 1;

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