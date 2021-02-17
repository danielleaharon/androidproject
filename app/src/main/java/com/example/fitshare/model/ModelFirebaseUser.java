package com.example.fitshare.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fitshare.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ModelFirebaseUser {
    public static final ModelFirebaseUser instance = new ModelFirebaseUser();
    private FirebaseAuth mAuth;
    public   FirebaseDatabase database;
    private DatabaseReference myRef;
    private String userID;
    private User user;

    private ModelFirebaseUser() {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    public User getUser() {
        return user;
    }

    public interface GetUserListener{
        void onComplete(User data);
    }
    public void UploadUserData(String id,final GetUserListener listener) {


        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        user=new User();
        userID = cleanUserName(id).trim();
        myRef = database.getReference().child("Users").child(id);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);
                listener.onComplete(user);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }
    public interface NewUserListener{
        void onComplete();
    }
    public void newUser(User user, NewUserListener listener) {


        this.user=user;
        userID=user.getId();
        myRef = database.getReference("Users");

        myRef.child(userID).setValue(user);
        listener.onComplete();


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
    public void changeLanguage(String language) {
        DatabaseReference myRef;
        myRef = database.getReference("Users");
        myRef.child(userID).child("language").setValue(language);
        user.setLanguage(language);

    }
    public void updateColorUser(int color) {
        DatabaseReference myRef;
        myRef = database.getReference("Users");
        myRef.child(userID).child("color").setValue(color);


    }

    public interface AllUserListener{
        void onComplete(List<String > data);
    }
        public void createAllUserList(AllUserListener listener) {
        DatabaseReference myRef;
        List<String> AllUsers= new ArrayList<>();
        myRef = database.getReference("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    User user = postSnapshot.getValue(User.class);
                    AllUsers.add(user.getEmail());
                }
                listener.onComplete(AllUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }




}
