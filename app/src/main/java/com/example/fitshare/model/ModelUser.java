package com.example.fitshare.model;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.fitshare.MainActivity;

import java.util.List;

public class ModelUser {
    public final static ModelUser instance = new ModelUser();
    User user;

    ModelUser() {

    }

    public interface GetAllUserListener {
        void onComplete(List<User> data);
    }

    public interface GetUserListener {
        void onComplete();
    }

    //    public void getAllUser( String UserID,final ModelUser.GetAllUserListener listener){
//        class MyAsyncTask extends AsyncTask {
//            List<User> data;
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                data = AppLocalUserDb.db.UserDao().getUser(UserID);
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
//    }
    public User getUser() {
        return user;
    }

    public void getUser(String UserID, final GetUserListener listener) {
        ModelFirebaseUser.instance.UploadUserData(UserID, new ModelFirebaseUser.GetUserListener() {
            @Override
            public void onComplete(User data) {

                user = data;
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {

                        AppLocalUserDb.db.UserDao().insertAll(data);

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

    public void RefreshUser(String UserID, final GetUserListener listener) {
        ModelFirebaseUser.instance.UploadUserData(UserID, new ModelFirebaseUser.GetUserListener() {
            @Override
            public void onComplete(User dataListener) {
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {

                        AppLocalUserDb.db.UserDao().insertAll(dataListener);
                        user = dataListener;

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

    public interface AddUserListener {
        void onComplete();
    }

    public void newUser(final User user, final ModelUser.AddUserListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalUserDb.db.UserDao().insertAll(user);
                ModelFirebaseUser.instance.newUser(user);
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


    public void UpdateUser(final String language, final ModelUser.AddUserListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalUserDb.db.UserDao().insertAll(user);
                ModelFirebaseUser.instance.changeLanguage(language);
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

    public String cleanUserName(String email) {
        StringBuilder temp = new StringBuilder();
        String[] tempList = email.split("@");
        String[] t = null;
        if (tempList[0].contains("."))
            t = tempList[0].split("\\.");
        else if (tempList[0].contains("_"))
            t = tempList[0].split("_");
        else temp.append(tempList[0]);


        String[] PointList = null;

        if (t != null)
            for (String s : t) {
                temp.append(s);
            }

        if (tempList.length >= 2) {
            PointList = tempList[1].split("\\.");
            temp.append(PointList[0]);
            temp.append(PointList[1]);
        }
        return temp.toString().trim();
    }

    public interface CheckUserExistsListener {
        void onComplete(Boolean bool);
    }

    public void CheckUserExists(String email, CheckUserExistsListener listener) {

        ModelFirebaseUser.instance.createAllUserList(new ModelFirebaseUser.AllUserListener() {
            @Override
            public void onComplete(List<String> data) {
                if (data.contains(email))
                    listener.onComplete(true);
                else listener.onComplete(false);
            }
        });

    }


}

