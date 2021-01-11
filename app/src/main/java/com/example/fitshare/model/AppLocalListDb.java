package com.example.fitshare.model;


import androidx.room.Database;


import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitshare.MyApplication;

@Database(entities = {myLists.class}, version = 3)
abstract class AppLocalListDbRepository extends RoomDatabase {
    public abstract myListDao myListDao();

}

public class AppLocalListDb {
    static public AppLocalListDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    AppLocalListDbRepository.class,
                    "dbFileNameLists.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
