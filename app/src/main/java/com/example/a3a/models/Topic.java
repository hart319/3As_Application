package com.example.a3a.models;

public class Topic {
    public String name;
    public boolean done;

    public Topic() {
    }

    public Topic(String name) {
        this.name = name;
        this.done = false;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                ", done=" + done +
                '}';
    }
}
