package com.example.assignment;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MeteorShowers.class}, version = 1)
public abstract class MeteorShowersDatabase extends RoomDatabase {
    public abstract MeteorShowersDao meteorShowersDao();
}
