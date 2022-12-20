package com.hikko.scheduleapp;

// Пара
public class Activity {
    public String start;
    public String end;
    public String name;
    public String type;

    public Activity(String name, String type, String start, String end) {
        this.name = name;
        this.type = type;
        this.start = start;
        this.end = end;
    }
}
