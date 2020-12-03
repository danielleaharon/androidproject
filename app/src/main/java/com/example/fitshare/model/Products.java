package com.example.fitshare.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class Products {

    public String name;
    public String img;
    public boolean selected;

   public Products (String name, boolean selected)
    {
        this.name=name;
        this.selected=false;

    }
    public Products ()
    {
        this.name="name";
        this.selected=false;

    }
    public  boolean isSelected()
    {
        return selected;
    }
    public  void setSelected(boolean selected)
    {
        this.selected=selected;
    }
    public String getName(){return this.name;}

}
