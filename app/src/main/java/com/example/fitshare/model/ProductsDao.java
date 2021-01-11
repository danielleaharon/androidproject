package com.example.fitshare.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductsDao {

    @Query("select * from Products Where listID==:listId and isDelete=0 ORDER BY selected")
    LiveData<List<Products>> getAllProducts(String listId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProduct(Products... products);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Products... products);

    @Query("DELETE FROM Products")
    void delete();

    @Delete
    void delete(Products products);
}
