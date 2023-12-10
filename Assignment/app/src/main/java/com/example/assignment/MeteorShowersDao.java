package com.example.assignment;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MeteorShowersDao {
    @Insert
    void insert(MeteorShowers meteorShowers);

    @Delete
    void delete(MeteorShowers meteorShowers);

    @Query("SELECT * FROM meteor_showers")
    List<MeteorShowers> getAllMeteorShowers();

    @Query("SELECT * FROM meteor_showers WHERE event = :event LIMIT 1")
    MeteorShowers getMeteorShowerByName(String event);
}
