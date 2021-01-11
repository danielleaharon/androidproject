package com.example.fitshare.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitshare.MyApplication;


    @Database(entities = {Products.class}, version = 3)
    abstract class AppLocalProductsDbRepository extends RoomDatabase {

        public abstract ProductsDao ProductsDao();
    }

    public class AppLocalProductDb {
        static public com.example.fitshare.model.AppLocalProductsDbRepository db =
                Room.databaseBuilder(MyApplication.context,
                        com.example.fitshare.model.AppLocalProductsDbRepository.class,
                        "dbFileNameProduct.db")
                        .fallbackToDestructiveMigration()
                        .build();
    }

