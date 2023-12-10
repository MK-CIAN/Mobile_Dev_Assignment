package com.example.assignment;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//Room Database for Meteor Showers
@Database(entities = {MeteorShowers.class}, version = 1)
public abstract class MeteorShowersDatabase extends RoomDatabase {

    //Setting the DAO method
    public abstract MeteorShowersDao meteorShowersDao();
}
