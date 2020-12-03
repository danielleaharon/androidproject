package com.example.fitshare.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    public String email;
    public List<myLists> myLists;
    public String id;

    public User(String email,String id)
    {
     this.email=email;
     this.id=id;
     this.myLists= new ArrayList<>();

    }
    public User()
    {
        this.email=email;
        this.id=id;
        myLists=new ArrayList<>();

    }
    public void CreateList(List<myLists> myLists)
    {
     this.myLists=myLists;
    }
    public void addtolist(myLists newList)
    {
        myLists.add(newList);

    }
    public List<com.example.fitshare.model.myLists> getMyLists()
    {
        return myLists;
    }
}
