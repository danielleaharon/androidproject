package com.example.fitshare.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface myListDao {

    boolean False=false;
    @Query("select * from myLists Where user==:userID and isDelete==0 ")
   LiveData< List<myLists>> getAllLists(String userID);

    @Update(onConflict = OnConflictStrategy.REPLACE)
     void updateMyLists(myLists... myLists);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(myLists... myLists);


    @Query("DELETE FROM myLists")
    void delete();
    @Delete
    void delete(myLists myLists);


}