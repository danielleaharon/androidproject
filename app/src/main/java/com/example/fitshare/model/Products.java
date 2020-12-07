package com.example.fitshare.model;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Products {

    public String name;

    public boolean selected;
    public Bitmap img;

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
    public void setImage(Bitmap bitmap)
    {
        this.img=bitmap;
    }

}
