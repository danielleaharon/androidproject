package com.example.fitshare.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class myLists {

    public String ListName;
    public String listID;

    public List<User> users=new ArrayList<>();

    public List<Products> Products=new ArrayList<>();

    public myLists(String ListName,String listID)
    {
        this.ListName=ListName;
        this.listID=listID;
        List<Products> Products=new ArrayList<>();



    }
    public myLists()
    {
        this.ListName="ddddd";

      //  createProductList();


    }
    public  void createProductList(List<Products> Products)
    {

        this.Products.addAll(Products);

    }
//@RequiresApi(api = Build.VERSION_CODES.N)
//public void SortProducts()
//{
//    Products.sort(new Comparator<com.example.fitshare.model.Products>() {
//        @Override
//        public int compare(com.example.fitshare.model.Products o1, com.example.fitshare.model.Products o2) {
//            if(o2.selected) return -1;
//            return 1;
//        }
//    });
//}
    public List<Products> getProductsList()
    {
        return Products;
    }
    public void addUser(User user){
        users.add(user);
    }
    public void addProducts(Products products){
        Products.add(products);
    }

}
