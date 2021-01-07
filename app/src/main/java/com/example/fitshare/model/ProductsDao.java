package com.example.fitshare.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductsDao {

    @Query("select * from Products Where listID==:listId")
    List<Products> getAllProducts(String listId);

    @Update
    void updateProduct(Products... products);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Products... products);

    @Delete
    void delete(Products products);
}
