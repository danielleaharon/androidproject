package com.example.fitshare.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.navigation.ActionOnlyNavDirections;

import com.example.fitshare.HomeActivity;
import com.example.fitshare.MainActivity;
import com.example.fitshare.R;
import com.example.fitshare.UploadFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ModelUser {
    public final static ModelUser instance = new ModelUser();
    User user;
    private FirebaseAuth mAuth;
    private MainActivity MainActivity;


    ModelUser() { }


    public void IfUserAlreadyLoggedin(){

        mAuth = FirebaseAuth.getInstance();
        //        //If the user has already logged in to the app, doesn't ask them to reconnect
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            openUploadPage();
            String userID = cleanUserName(user.getEmail());
            getUser(userID, new ModelUser.GetUserListener() {
                @Override
                public void onComplete() {
                    openLists();
                }
            });
        }
    }

    public void setActivity(MainActivity activity)
    {
        MainActivity=activity;
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
        ModelFirebaseUser.instance.newUser(user, new ModelFirebaseUser.NewUserListener() {
            @Override
            public void onComplete() {
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        AppLocalUserDb.db.UserDao().insertAll(user);
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
        });

    }

    public void UpdateUserLanguage(final User user , final ModelUser.AddUserListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {

                AppLocalUserDb.db.UserDao().insertAll(user);
                ModelFirebaseUser.instance.changeLanguage(user.getLanguage());
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

    public void UpdateUserColor(final User user , final ModelUser.AddUserListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                user.setColor(user.getColor());
                AppLocalUserDb.db.UserDao().insertAll(user);
                ModelFirebaseUser.instance.updateColorUser(user.getColor());
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

    public void ForgotPassword(String email)
    {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity, "Email sent", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "Email sent.");
                        }
                    }
                });
    }

   public void SignIn(final String email, final String pass) {

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Log.d("TAG", "Login successful ");

                    openUploadPage();
                    String userID = cleanUserName(email);
                    getUser(userID, new ModelUser.GetUserListener() {
                        @Override
                        public void onComplete() {
                            openLists();
                        }
                    });


                } else {
                    task.getException().printStackTrace();
                    Register(email, pass);
                    Log.d("TAG", "Login failed ");

                }
            }
        });

    }
   public void Register(final String email, final String pass) {

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "new user: " + email + "  " + pass);
                    Toast.makeText(MainActivity, email + ", welcome ", Toast.LENGTH_LONG).show();

                    String email1 = email.toLowerCase();
                    String userID = cleanUserName(email);
                    User user = new User(email, userID, "Hebrew", Color.rgb(243,214,104));
                    newUser(user, new ModelUser.AddUserListener() {
                        @Override
                        public void onComplete() {
                            SignIn(email1, pass);
                        }
                    });


                } else {
                    task.getException().printStackTrace();
                    Toast.makeText(MainActivity, "הסיסמא לא נכונה ", Toast.LENGTH_LONG).show();
                    Log.d("TAG", "Failed to create new user");

                }
            }
        });

    }
    public void openLists() {

        Intent intent = new Intent(MainActivity, HomeActivity.class);
        MainActivity.startActivity(intent);
        MainActivity.finish();

    }

    public void openUploadPage() {
        hideKeyboard();
        UploadFragment UploadFragment = new UploadFragment();
        UploadFragment.parent =  MainActivity;
        FragmentTransaction transaction = MainActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main, UploadFragment, "TAG");
        transaction.addToBackStack("TAG");
        transaction.commit();
    }
    public void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(MainActivity.getWindow().getDecorView().getWindowToken(), 0);

    }
    public void Logout(HomeActivity homeActivity) {
        mAuth.signOut();
        Intent intent = new Intent(homeActivity, MainActivity.getClass());
        homeActivity.startActivity(intent);
        homeActivity.finish();
    }
}

