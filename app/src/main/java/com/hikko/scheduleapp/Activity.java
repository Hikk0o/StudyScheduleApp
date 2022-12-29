package com.hikko.scheduleapp;

// Пара
public class Activity {
    public String start;
    public String end;
    public String name;
    public String type;
    public int id;

    public Activity(String name, String type, String start, String end) {
        this.name = name;
        this.type = type;
        this.start = start;
        this.end = end;
        this.id = -1;
    }
    public Activity(String name, String type, String start, String end, int id) {
        this.name = name;
        this.type = type;
        this.start = start;
        this.end = end;
        this.id = id;
    }
}
