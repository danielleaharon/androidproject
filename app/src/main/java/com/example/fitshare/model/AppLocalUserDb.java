package com.example.fitshare.model;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitshare.MyApplication;

@Database(entities = {User.class}, version = 2)
abstract class AppLocalUserDbRepository extends RoomDatabase {
    public abstract UserDao UserDao();

}

public class AppLocalUserDb {
    static public AppLocalUserDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    AppLocalUserDbRepository.class,
                    "dbFileNameUser.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
