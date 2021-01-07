package com.example.fitshare.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ModelListDao {

    public final static ModelListDao instance = new ModelListDao();
    private ModelListDao(){

    }

    public interface GetAllMyListsListener{
        void onComplete(List<myLists> data);
    }

    public void RefreshMyList(){
        ModelFirebase.instance.RefreshMyLists();

    }
    public  LiveData<List<myLists>>  getAllMyLists(String userID){

        LiveData<List<myLists>> liveData=AppLocalListDb.db.myListDao().getAllLists(userID);
//        class MyAsyncTask extends AsyncTask {
//            List<myLists> data;
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                data = AppLocalListDb.db.myListDao().getAllLists(userID);
////                try {
////                    Thread.sleep(5000);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                super.onPostExecute(o);
//                listener.onComplete(data);
//            }
//        }
//        MyAsyncTask task = new MyAsyncTask();
//        task.execute();
        RefreshMyList();
        return liveData;
    }

    public interface AddMyListsListener{
        void onComplete();
    }
    public void addMyList(final myLists myLists, final AddMyListsListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalListDb.db.myListDao().insertAll(myLists);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
    public interface deleteMyListsListener{
        void onComplete();
    }
    public void deleteMyList(final myLists myLists, final deleteMyListsListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalListDb.db.myListDao().delete(myLists);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public interface updateMyListsListener{
        void onComplete();
    }
    public void updateMyList(final myLists myLists, final updateMyListsListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalListDb.db.myListDao().updateMyLists(myLists);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}
