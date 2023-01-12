package com.hikko.scheduleapp;

// Пара
public class Activity implements Comparable<Activity> {
    public String start_time;
    public String end_time;
    public String name;
    public String type;
    public int id;

    public Activity(String name, String type, String start, String end, int id) {
        this.name = name;
        this.type = type;
        this.start_time = start;
        this.end_time = end;
        this.id = id;
    }
    public Activity(String name, String type, String start, String end) {
        this.name = name;
        this.type = type;
        this.start_time = start;
        this.end_time = end;
        this.id = -1;
    }

    // for sorting
    @Override
    public int compareTo(Activity o) {
        return toString().compareTo(o.start_time);
    }
}
