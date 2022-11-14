package com.example.a3a.models;

import java.util.HashMap;

public class User {
    public String uid;
    public String name;
    public String email;
    public int year;
    public HashMap<String, Boolean> joinedGroups;
    public HashMap<String, Boolean> group;

    public User() {
    }

    public User(String uid, String name, String email, int year) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.year = year;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", year=" + year +
                ", joinedGroups=" + joinedGroups +
                ", group=" + group +
                '}';
    }
}
