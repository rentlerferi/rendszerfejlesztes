package com.example.api;

public class Tool{

    private String name;
    private int id;
    private String location;
    private int repair_period;
    private int repair_time;
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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
        return repair_period;
    }

    public void setRepare_period(int repare_period) {
        this.repair_period = repare_period;
    }

    public int getRepare_time() {
        return repair_time;
    }

    public void setRepare_time(int repare_time) {
        this.repair_time = repare_time;
    }

    public Tool(String name, int id, String location, String category){
        this.name = name;
        this.id = id;
        this.location = location;
        this.category = category;
        //this.repair_period = repare_period;
        //this.repair_time = repare_time;
    }

}
