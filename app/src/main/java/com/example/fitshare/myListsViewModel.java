package com.example.fitshare;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.fitshare.model.ModelListDao;
import com.example.fitshare.model.myLists;

import java.util.ArrayList;
import java.util.List;

public class myListsViewModel extends ViewModel {
   private LiveData< List<myLists> >data;


    public LiveData<  List<myLists> >getData(String UserId){
        if(data==null)
            data= ModelListDao.instance.getAllMyLists(UserId);
        return  this.data;


    }

    public void Refresh() {
        ModelListDao.instance.RefreshMyList();
    }
}
