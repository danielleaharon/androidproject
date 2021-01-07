package com.example.fitshare.model;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserListDao {


        @Query("SELECT * from myLists")
        public List<UserLists> getListWithUser();


//    @Insert
//    void insert(UserLists UserLists);
//
//    @Query("SELECT * FROM user INNER JOIN  User_Lists_join ON id==userId WHERE myListsId=:myListsId")
////            user.id==User_Lists_join.userId WHERE
////            user_repo_join.myListsId=:myListsId")
//    List<User> getUsersForList(final String myListsId);
//
//
//    @Query("SELECT * FROM myLists INNER JOIN User_Lists_join ON listID==myListsId WHERE userId==:userId")
//            List<myLists> getListForUsers(final String userId);

}
