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
    public void CreateList()
    {
        this.myLists= new ArrayList<>();

        myLists.add(new myLists("one list",this));
        myLists.add(new myLists("two list",this));
        myLists.add(new myLists("tree list",this));
        myLists.add(new myLists("four list",this));
    }
    public void addtolist(String listname)
    {
        myLists.add(new myLists(listname,this));
    }
}
