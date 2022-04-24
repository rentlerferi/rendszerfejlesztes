package com.example.api;

public class Category {

    private String name;
    private String interval;
    private String requirements;
    private int norma;

    public Category(String interval, String instructions, int norma) {
        //this.name = name;
        this.interval = interval;
        this.instructions = instructions;
        this.norma = norma;
    }

    public Category() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getNorma() {
        return norma;
    }

    public void setNorma(int norma) {
        this.norma = norma;
    }
}
