package com.example.api;

import java.util.ArrayList;

public class User {
    public String role;
    public String name;
    private ArrayList<String> professions = new ArrayList<>();
    public int shiftTime;

    public User() {
    }

    public User(ArrayList<String> professions, String name, String role, int shiftTime) {
        this.name = name;
        this.role = role;
        this.professions = professions;
        this.shiftTime = shiftTime;
    }

    public ArrayList<String> getProfessions() {
        return professions;
    }

    public void addProfession(String prof) { professions.add(prof); }

    public void removeProfession(String prof) { professions.remove(prof); }
}