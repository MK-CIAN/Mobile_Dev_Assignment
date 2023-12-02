package com.example.assignment;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DarkSkyReserve.class}, version = 1)
public abstract class DarkSkyDatabase extends RoomDatabase {
    public abstract DarkSkyReserveDao darkSkyReserveDao();
}
