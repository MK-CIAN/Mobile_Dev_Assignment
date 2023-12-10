package com.example.assignment;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Room Entity class representing Dark Sky Reserves
@Entity(tableName = "dark_sky_reserves")
public class DarkSkyReserve {
    // Primary key with auto-generated value
    @PrimaryKey(autoGenerate = true)
    public long id;

    // Columns for reserve details
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "color")
    public String color;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;

    // Getter and setter methods for each field
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
