package com.example.fitshare.model;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import java.util.ArrayList;
import java.util.List;

@Entity

public class User {
    @NonNull
    @PrimaryKey
    private String id;
    private String email;
    private String language;
    private int color;




    public User(String email, String id, String language, int color) {
        this.email = email;
        this.id = id;
        this.language = language;
        this.color= color;

    }

    @Ignore
    public User() {
        id = null;
    }

    public int getColor() { return color; }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setId( String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setColor(int color) { this.color = color; }



}
