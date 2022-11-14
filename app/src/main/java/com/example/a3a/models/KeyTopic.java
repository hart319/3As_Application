package com.example.a3a.models;

public class KeyTopic {
    public String key;
    public String name;
    public boolean done;

    public KeyTopic() {
    }

    @Override
    public String toString() {
        return "KeyTopic{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", done=" + done +
                '}';
    }
}
