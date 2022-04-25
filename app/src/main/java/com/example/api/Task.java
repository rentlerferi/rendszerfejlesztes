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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean emergency) {
        isEmergency = emergency;
    }

    public String getRepairerID() {
        return repairerID;
    }

    public void setRepairerID(String repairerID) {
        this.repairerID = repairerID;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

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