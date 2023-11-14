package com.example.assignment;

public class MeteorShowers {
    private String date;
    private String event;
    private String constellation;
    private String meteorsPerHour;
    private String description;

    public MeteorShowers(String event, String date, String constellation, String meteorsPerHour, String description) {
        this.date = date;
        this.event = event;
        this.constellation = constellation;
        this.meteorsPerHour = meteorsPerHour;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getMeteorsPerHour() {
        return meteorsPerHour;
    }

    public void setMeteorsPerHour(String meteorsPerHour) {
        this.meteorsPerHour = meteorsPerHour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
