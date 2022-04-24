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

    public Task(){
    }

    public Task(String itemName, String location, String instruction, String date, String status) {
        this.itemName = itemName;
        this.location = location;
        this.instruction = instruction;
        this.date = date;
        this.status = status;
    }
    public Task(String itemName, String location, String instruction, String date, String status, boolean isEmergency) {
        this.itemName = itemName;
        this.location = location;
        this.instruction = instruction;
        this.date = date;
        this.status = status;
        this.isEmergency = isEmergency;
    }
}