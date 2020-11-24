package com.example.fitshare.model;

public class Proprties {
    public String name;
    public boolean isSelected;
    public int drawable;


    public Proprties(String name, int d)
    {
        this.name=name;
        isSelected= false;
        this.drawable=d;
    }


    public  boolean isSelected()
    {
        return isSelected;
    }
    public  void setSelected(boolean s)
    {
        isSelected=s;
    }
    public String getName(){return this.name;}
}
