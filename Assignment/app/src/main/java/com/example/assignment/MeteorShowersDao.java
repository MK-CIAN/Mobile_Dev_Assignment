package com.example.assignment;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// Room DAO (Data Access Object) annotation
@Dao
public interface MeteorShowersDao {
    // Insert method to add a MeteorShowers object to the database
    @Insert
    void insert(MeteorShowers meteorShowers);

    // Delete method to remove a MeteorShowers object from the database
    @Delete
    void delete(MeteorShowers meteorShowers);

    // Query to retrieve all MeteorShowers objects from the database
    @Query("SELECT * FROM meteor_showers")
    List<MeteorShowers> getAllMeteorShowers();

    // Query to retrieve a specific MeteorShower by its event name used to ensure no duplicate values in saved events
    @Query("SELECT * FROM meteor_showers WHERE event = :event LIMIT 1")
    MeteorShowers getMeteorShowerByName(String event);
}
