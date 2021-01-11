package com.example.fitshare;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.fitshare.model.ModelList;
import com.example.fitshare.model.myLists;
import java.util.List;

public class myListsViewModel extends ViewModel {
   private LiveData< List<myLists> >data;

    public interface GetAllMyListsListener{
        void onComplete(LiveData<List<myLists> >data);
    }
    public void getData(GetAllMyListsListener listener){
        if(data==null) {
            ModelList.instance.getAllMyLists(new ModelList.GetAllMyListsListener() {
                @Override
                public void onComplete(LiveData<List<myLists>> ldata) {
                    data = ldata;
                    listener.onComplete(data);

                }
            });
        }
        else listener.onComplete(data);

    }


    public interface RefreshMyListsListener{
        void onComplete();
    }
    public void Refresh(RefreshMyListsListener listener) {
        ModelList.instance.RefreshMyList(new ModelList.GetRefreshMyListListener() {
            @Override
            public void onComplete() {

                listener.onComplete();
            }
        });
    }
}
