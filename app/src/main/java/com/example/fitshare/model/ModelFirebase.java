package com.example.fitshare.model;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelFirebase {
    FirebaseDatabase database;
    Map<String ,String> UserMap=new HashMap<>();
    DatabaseReference myRef ;
     FirebaseAuth mAuth;
   public ModelFirebase()
    {
        database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();

    }

    public void newUser(String email)
    {
         myRef = database.getReference("Users");
        String userID = myRef.push().getKey();

        User user= new User(email, userID );
        myRef.child(mAuth.getCurrentUser().getUid()).setValue(user);


    }
    public FirebaseAuth getmAuth()
    {
        return mAuth;
    }
    public String getUserId(String authID){
       return mAuth.getCurrentUser().getUid();

    }
}
