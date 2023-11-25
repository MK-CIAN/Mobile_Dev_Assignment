package com.example.assignment;

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
}
