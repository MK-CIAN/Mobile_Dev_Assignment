package com.example.assignment;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Room Entity annotation to specify table name
@Entity(tableName = "meteor_showers")
public class MeteorShowers {
    // PrimaryKey with autoGenerate
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Fields representing columns in the database table
    private String date;
    private String event;
    private String constellation;
    private String meteorsPerHour;
    private String description;

    // Constructor to initialize the MeteorShowers object
    public MeteorShowers(String event, String date, String constellation, String meteorsPerHour, String description) {
        this.date = date;
        this.event = event;
        this.constellation = constellation;
        this.meteorsPerHour = meteorsPerHour;
        this.description = description;
    }

    // Getter and setter methods for each field

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

    // Getter and setter for the primary key
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
