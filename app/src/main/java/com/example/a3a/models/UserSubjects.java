package com.example.a3a.models;

public class UserSubjects {
    public String uid;
    public String name;
    public Subject phy;
    public Subject chem;
    public Subject cm;
    public Subject bio;

    public UserSubjects() {
    }

    public UserSubjects(String uid, String name, boolean isPhy, boolean isChem, boolean isCM, boolean isBio) {
        this.uid = uid;
        this.name = name;
        this.phy = new Subject("Physics", isPhy);
        this.chem = new Subject("Chemistry", isChem);
        this.cm = new Subject("Maths", isCM);
        this.bio = new Subject("Biology", isBio);
    }

    @Override
    public String toString() {
        return "UserSubjects{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", phy=" + phy +
                ", chem=" + chem +
                ", cm=" + cm +
                ", bio=" + bio +
                '}';
    }
}
