package com.example.api;

import java.util.ArrayList;

public class User {
    public String role;
    public String name;
    private ArrayList<String> professions = new ArrayList<>();

    public User() {
    }

    public User(ArrayList<String> professions, String name, String role) {
        this.name = name;
        this.role = role;
        this.professions = professions;
    }

    @Override
    public String toString() {
        return "User{" +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", profession=" + professions +
                '}';
    }

    public ArrayList<String> getProfessions() {
        return professions;
    }

    public void addProfession(String prof) { professions.add(prof); }

    public void removeProfession(String prof) { professions.remove(prof); }
}