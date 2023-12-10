package com.example.assignment;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// Room DAO (Data Access Object) for Dark Sky Reserves
@Dao
public interface DarkSkyReserveDao {
    // Query to retrieve all Dark Sky Reserves from the database
    @Query("SELECT * FROM dark_sky_reserves")
    List<DarkSkyReserve> getALL();

    // Insert method to add a new Dark Sky Reserve to the database
    @Insert
    void insert(DarkSkyReserve reserve);
}
