package com.example.api;

import java.util.Date;

public class Task {
    public String itemName;
    public String location;
    public String instruction;
    public Date date;
    public String repairerID;

    public Task(){
    }

    public Task(String itemName, String location, String instruction, Date date) {
        this.itemName = itemName;
        this.location = location;
        this.instruction = instruction;
        this.date = date;
    }
}
