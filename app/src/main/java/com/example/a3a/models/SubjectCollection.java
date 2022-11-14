package com.example.a3a.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectCollection {
    public SubjectSet Biology;
    public SubjectSet Chemistry;
    public SubjectSet Maths;
    public SubjectSet Physics;

    public SubjectCollection() {
    }

    @Override
    public String toString() {
        return "SubjectCollection{" +
                "Biology=" + Biology +
                ", Chemistry=" + Chemistry +
                ", Maths=" + Maths +
                ", Physics=" + Physics +
                '}';
    }
}

