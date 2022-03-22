package com.example.api;

public class Tool{

    private String name;
    private int id;
    private String location;
    private int repare_period;
    private int repare_time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRepare_period() {
        return repare_period;
    }

    public void setRepare_period(int repare_period) {
        this.repare_period = repare_period;
    }

    public int getRepare_time() {
        return repare_time;
    }

    public void setRepare_time(int repare_time) {
        this.repare_time = repare_time;
    }

    public Tool(String name, int id, String location, int repare_period, int repare_time){
        this.name = name;
        this.id = id;
        this.location = location;
        this.repare_period = repare_period;
        this.repare_time = repare_time;
    }

}
