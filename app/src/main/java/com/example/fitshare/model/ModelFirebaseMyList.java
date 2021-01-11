package com.example.fitshare.model;


import android.util.Log;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ModelFirebaseMyList {
    public static final ModelFirebaseMyList instance = new ModelFirebaseMyList();
    FirebaseDatabase database;
    DatabaseReference myRef;
    User user;
    List<myLists> myLists = new ArrayList<>();

    private ModelFirebaseMyList() {
        database = FirebaseDatabase.getInstance();

    }

    public interface GetListListener {
        void onComplete(List<myLists> data);
    }

    public void getUserListsData(GetListListener listener) {

        user = ModelUser.instance.getUser();
        myRef = database.getReference("Users").child(user.getId()).child("lists");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    myLists myList = (myLists) postSnapshot.getValue(myLists.class);
                    myLists.add(myList);

                }
                listener.onComplete(myLists);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public interface newListListener {
        void onComplete(myLists data);
    }

    public void newList(String name, List<String> userList, newListListener listener) {

        DatabaseReference myRef;
        DatabaseReference tempRef;
        myRef = database.getReference("Users");
        tempRef = database.getReference("AllLists");

        myRef = myRef.child(user.getId()).child("lists");
        String listID = myRef.push().getKey();

        myLists myNewList = new myLists(name, listID, 0);
        myNewList.setUser(user.getId());

        tempRef.child(listID).setValue(myNewList);
        for (String string : userList) {
            addUserToList(string, myNewList);
        }

        listener.onComplete(myNewList);

    }


    public User getUser() {
        return user;
    }


    public void removeList(myLists myLists) {
        DatabaseReference tempRef;

        myRef = database.getReference("Users");
        tempRef = database.getReference("AllLists");

        tempRef = tempRef.child(myLists.getListID()).child("user");

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String id = (postSnapshot.getKey());
                    myRef.child(id).child("lists").child(myLists.getListID()).child("isDelete").setValue(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tempRef = database.getReference("AllLists");
        tempRef.child(myLists.getListID()).child("isDelete").setValue(true);


    }

    public interface UserListListener {
        void onComplete(List<String> data);
    }

    public void getUserOfList(String listID, UserListListener listListener) {
        DatabaseReference myRef;
        DatabaseReference tempRef;
        List<String> listUser = new ArrayList<>();
        myRef = database.getReference("AllLists");
        myRef = myRef.child(listID).child("user");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    listUser.add(postSnapshot.getValue(String.class));
                }

                listListener.onComplete(listUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + databaseError.getMessage());

            }


        });

    }



    public void deleteUserFromList(String email, String listID) {
        DatabaseReference tempRef;
        DatabaseReference myRef;
        String userID = ModelUser.instance.cleanUserName(email);
        myRef = database.getReference("Users");
        tempRef = database.getReference("AllLists");

        myRef.child(userID).child("lists").child(listID).removeValue();
        tempRef.child(listID).child("user").child(userID).removeValue();


    }

    public void addUserToList(String email, myLists list) {
        DatabaseReference tempRef;
        DatabaseReference myRef;
        String userID = ModelUser.instance.cleanUserName(email);
        list.setUser(userID);
        myRef = database.getReference("Users");
        myRef.child(userID).child("lists").child(list.getListID()).setValue(list);
        tempRef = database.getReference("AllLists");
        tempRef.child(list.getListID()).child("user").child(userID).setValue(email);
    }


}
