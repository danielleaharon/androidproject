package com.example.fitshare.model;

import android.os.AsyncTask;

import java.util.List;

public class ModelUserDao {
    public final static ModelUserDao instance = new ModelUserDao();
    ModelUserDao()
    {

    }
    public interface GetAllUserListener{
        void onComplete(List<User> data);
    }

    public void getAllUser( String UserID,final ModelUserDao.GetAllUserListener listener){
        class MyAsyncTask extends AsyncTask {
            List<User> data;
            @Override
            protected Object doInBackground(Object[] objects) {
                data = AppLocalUserDb.db.UserDao().getUser(UserID);
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                listener.onComplete(data);
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public interface AddUserListener{
        void onComplete();
    }
    public void addUser(final User user, final ModelUserDao.AddUserListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalUserDb.db.UserDao().insertAll(user);
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
    public interface deleteUserListener{
        void onComplete();
    }
    public void deleteProducts(final User user, final ModelUserDao.deleteUserListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalUserDb.db.UserDao().delete(user);
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
