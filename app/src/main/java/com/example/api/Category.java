package com.example.api;

public class Category {

    public String name;
    public String interval;
    public String instructions = "";

    public Category(String interval, String instructions) {
        this.interval = interval;
        this.instructions = instructions;
    }

    public Category() { }
}
