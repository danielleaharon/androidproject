package com.example.fitshare.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class myLists {

    public String ListName;
    public String listID;
public int listCount;
    public List<User> users=new ArrayList<>();

    public List<Products> Products=new ArrayList<>();

    public myLists(String ListName,String listID,int listCount)
    {
        this.ListName=ListName;
        this.listID=listID;
        this.listCount=listCount;
        List<Products> Products=new ArrayList<>();



    }
    public myLists()
    {
        this.ListName="list";


    }
    public  void createProductList(List<Products> Products)
    {

        this.Products.addAll(Products);

    }




}
