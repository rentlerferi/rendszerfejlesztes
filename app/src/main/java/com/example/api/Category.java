package com.example.api;

public class Category {

    public String name;
    public String interval;
    public String instructions = "";
    public int norma;

    public Category(String interval, String instructions, int norma) {
        this.interval = interval;
        this.instructions = instructions;
        this.norma = norma;
    }

    public Category() { }
}
