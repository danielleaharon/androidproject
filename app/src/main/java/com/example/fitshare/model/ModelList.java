package com.example.fitshare.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ModelList {

    public final static ModelList instance = new ModelList();
    List<myLists> data;

    private ModelList() {

    }

    public interface GetAllMyListsListener {
        void onComplete(LiveData<List<myLists>> data);
    }

    public interface GetRefreshMyListListener {
        void onComplete();
    }

    public void RefreshMyList(GetRefreshMyListListener listener) {
        ModelFirebaseMyList.instance.getUserListsData(new ModelFirebaseMyList.GetListListener() {
            @Override
            public void onComplete(List<myLists> ldata) {
                data = ldata;
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {


                            for (myLists my : ldata) {
                                AppLocalListDb.db.myListDao().insertAll(my);
                            }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if (listener != null) {
                            listener.onComplete();
                        }
                    }

                }
                MyAsyncTask task = new MyAsyncTask();
                task.execute();
            }
        });


    }


    public void getAllMyLists(GetAllMyListsListener listener) {
        User user = ModelUser.instance.getUser();
        LiveData<List<myLists>> liveData = AppLocalListDb.db.myListDao().getAllLists(user.getId());
        RefreshMyList(null);
        listener.onComplete(liveData);

    }

    public interface AddMyListsListener {
        void onComplete();
    }

    public void addMyList(final String myLists, List<String> userList, final AddMyListsListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                ModelFirebaseMyList.instance.newList(myLists, userList, new ModelFirebaseMyList.newListListener() {
                    @Override
                    public void onComplete(com.example.fitshare.model.myLists data) {
                        AppLocalListDb.db.myListDao().insertAll(data);
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete();
                }
            }
        }

        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public interface deleteMyListsListener {
        void onComplete();
    }

    public void deleteMyList(final myLists myLists, final deleteMyListsListener listener) {
        ModelFirebaseMyList.instance.removeList(myLists);
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalListDb.db.myListDao().delete(myLists);


                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete();
                }
            }
        }
        ;
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public interface updateMyListsListener {
        void onComplete();
    }

    public void updateMyList(final myLists myLists, final updateMyListsListener listener) {

        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                ModelFirebaseMyList.instance.UpdateMyList(myLists);
                AppLocalListDb.db.myListDao().updateMyLists(myLists);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete();
                }
            }
        }
        ;
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public interface UserListsListener {
        void onComplete(List<String> data);
    }

    public void getUserList(String listID, UserListsListener listener) {
        ModelFirebaseMyList.instance.getUserOfList(listID, new ModelFirebaseMyList.UserListListener() {
            @Override
            public void onComplete(List<String> data) {
                listener.onComplete(data);
            }
        });
    }

    public interface addUserListsListener {
        void onComplete();
    }
    public void addUserToList(String email, myLists myLists,addUserListsListener listener) {
        ModelFirebaseMyList.instance.addUserToList(email, myLists);
         listener.onComplete();
    }

    public void DeleteUserToList(String email, myLists myLists) {
        ModelFirebaseMyList.instance.deleteUserFromList(email, myLists.getListID());

    }

    public void deleteAll() {
        AppLocalListDb.db.myListDao().delete();
    }
}
