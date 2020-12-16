package com.example.fitshare.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class User {

    public String email;
    public List<myLists> myLists=new ArrayList<>();
    public String id;
    public String language;

    public User(String email, String id,String language) {
        this.email = email;
        this.id = id;
        this.myLists = new ArrayList<>();
        this.language=language;

    }

    public User() {
//        this.email = email;
//        this.id = id;
//        myLists = new ArrayList<>();

    }

    public void addtolist(myLists newList) {
        myLists.add(newList);

    }

    public List<myLists> getMyLists() {

        return this.myLists;

    }
    public void removeFromListByName(String listName)
    {
        for (myLists list:myLists) {
            if(list.ListName.equals(listName))
                myLists.remove(list);

        }
    }
    public void updateListName(String oldName,String newName){

        for (int i=0; i<myLists.size();i++)
        {
            if(myLists.get(i).listID.equals(oldName))
                myLists.get(i).ListName="newName";

        }

    }
}
