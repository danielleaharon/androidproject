package com.example.fitshare.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Entity
public class myLists {

    @NonNull
    @PrimaryKey
    private String listID;
    private String ListName;
    private int listCount;
    private String User;
    private boolean isDelete;


    public myLists(String ListName, String listID, int listCount) {
        this.ListName = ListName;
        this.listID = listID;
        this.listCount = listCount;
        this.isDelete = false;

    }


    public Boolean getIsDelete() {
        return this.isDelete;
    }

    public String getListName() {
        return this.ListName;
    }

    public String getUser() {
        return this.User;
    }

    public String getListID() {
        return this.listID;
    }

    public int getListCount() {
        return this.listCount;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public void setListName(String listName) {
        this.ListName = listName;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    public void setListCount(int listCount) {
        this.listCount = listCount;
    }

    public void setUser(String User) {
        this.User = User;
    }


    @Ignore
    public myLists() {
    }


}
