package com.example.fitshare.model;

import java.util.ArrayList;
import java.util.List;

public class myLists {

    public String ListName;

    public List<User> users=new ArrayList<>();

    public List<Products> Products;

    myLists(String ListName,User user)
    {
        this.ListName=ListName;
        users.add(user);

    }

}
