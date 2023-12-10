package com.example.assignment;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// Room Database class for Dark Sky Reserves
@Database(entities = {DarkSkyReserve.class}, version = 1)
public abstract class DarkSkyDatabase extends RoomDatabase {
    // Abstract method to access DarkSkyReserveDao
    public abstract DarkSkyReserveDao darkSkyReserveDao();
}

