package com.example.fitshare.model;

import java.io.Serializable;

public class Products implements Serializable {

    public String name;
    public String Amount;
    public boolean selected;
    public String imgUrl;

   public Products (String name, boolean selected,String Url,String amount)
    {
        this.name=name;
        this.selected=selected;
        this.imgUrl=Url;
       this.Amount=amount;

    }
    public Products ()
    {
        this.name="name";
        this.selected=false;
        this.Amount="1";

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
    public void setImage(String url)
    {
        this.imgUrl=url;
    }



}
