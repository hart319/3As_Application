package com.example.a3a.models;

import java.util.HashMap;
import java.util.Map;

public class SubjectSet {
    public HashMap<String, String> Grade_12;
    public HashMap<String, String> Grade_13;

    public SubjectSet() {
    }

    public HashMap<String, Topic> to_topics() {
        HashMap<String, Topic> temp = new HashMap();

        for (Map.Entry<String, String> entry : Grade_12.entrySet()) {
            temp.put(entry.getKey(), new Topic(entry.getValue()));
        }

        for (Map.Entry<String, String> entry : Grade_13.entrySet()) {
            temp.put(entry.getKey(), new Topic(entry.getValue()));
        }

        return temp;
    }

    @Override
    public String toString() {
        return "SubjectSet{" +
                "Grade_12=" + Grade_12 +
                ", Grade_13=" + Grade_13 +
                '}';
    }
}
