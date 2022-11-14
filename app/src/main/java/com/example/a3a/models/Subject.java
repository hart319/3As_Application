package com.example.a3a.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subject {
    public String name;
    public boolean isDoing;
    public HashMap<String, Topic> topics;

    public Subject() {
    }

    public Subject(String name, boolean isDoing) {
        this.name = name;
        this.isDoing = isDoing;
        this.topics = new HashMap<String, Topic>();
    }

    public double calcPercentage() {
        int noOfTopics = this.topics.size();
        int completed = 0;


        for (Map.Entry<String, Topic> entry: this.topics.entrySet()) {
            if (entry.getValue().done) {
                completed++;
            }
        }

        double per = ((double) completed) * 100 / noOfTopics;
        return per;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                ", isDoing='" + isDoing + '\'' +
                ", topics=" + topics +
                '}';
    }
}

