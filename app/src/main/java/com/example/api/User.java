package com.example.api;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class User{
    private String role;
    private String name;
    private ArrayList<String> profession;

    public User(){
    }

    public User(ArrayList<String> profession, String name, String role) {
        this.name = name;
        this.role = role;
        this.profession = profession;
    }

    @Override
    public String toString() {
        return "User{" +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", profession=" + profession +
                '}';
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getProfession() {
        return profession;
    }

    public void setProfession(ArrayList<String> profession) {
        this.profession = profession;
    }
}