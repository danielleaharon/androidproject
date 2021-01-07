package com.example.fitshare.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from user Where id== :userID")
    List<User> getUser(String userID);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... User);

    @Delete
    void delete(User User);
}
