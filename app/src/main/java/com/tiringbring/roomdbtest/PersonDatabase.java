package com.tiringbring.roomdbtest;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Person.class}, version = 1, exportSchema = false)
public abstract class PersonDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
}
