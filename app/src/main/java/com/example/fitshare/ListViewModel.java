package com.example.fitshare;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.fitshare.model.ModelFirebase;
import com.example.fitshare.model.myLists;

import java.util.List;

public class ListViewModel extends ViewModel {

    LiveData<List<myLists>> liveData;

    public  LiveData<List<myLists>> getData()
    {
       // if(liveData!=null)
          //  liveData= ModelFirebase.instance.getUser().getMyLists();
        return liveData;
    }
}
