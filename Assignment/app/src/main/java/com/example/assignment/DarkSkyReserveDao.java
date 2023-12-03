package com.example.assignment;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DarkSkyReserveDao {
    @Query("SELECT * FROM dark_sky_reserves")
    List<DarkSkyReserve> getALL();

    @Insert
    void insert(DarkSkyReserve reserve);
}
