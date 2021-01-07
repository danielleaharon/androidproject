package com.example.fitshare.model;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.Relation;

import java.util.List;

public class UserLists {
    @Embedded public myLists myLists;

    @Relation(parentColumn = "listID",
            entityColumn = "id") public List<User> UserList;
    UserLists()
    {

    }
}

//@Entity(tableName = "User_Lists_join",
//        primaryKeys = { "userId", "myListsId" },
//        foreignKeys = {
//                @ForeignKey(entity = User.class,
//                        parentColumns = "id",
//                        childColumns = "userId"),
//                @ForeignKey(entity = myLists.class,
//                        parentColumns = "id",
//                        childColumns = "myListsId")
//        })
//public class UserLists {
//
//
//        public final String userId;
//        public final String myListsId;
//
//        public UserLists(final String userId, final String myListsId) {
//            this.userId = userId;
//            this.myListsId = myListsId;
//        }

//    UserLists(String userId, String myListsId)
//    {
//        this.UserId=userId;
//        this.myListsId=myListsId;
//    }
//
//    public String getUserId(){return this.UserId;}
//    public String getMyListsId(){return this.myListsId;}

//    public void setUserId(String userId){this.UserId=userId;}
//    public void setMyListsId(String myListsId){this.myListsId=myListsId;}


