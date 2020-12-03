package com.example.fitshare.model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitshare.HomeActivity;
import com.example.fitshare.MainActivity;
import com.example.fitshare.MylistFragmentDirections;
import com.example.fitshare.ProductsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

    public static final ModelFirebase instance =new ModelFirebase();

    FirebaseDatabase database;
    Map<String ,String> UserMap=new HashMap<>();
    DatabaseReference myRef ;
    DatabaseReference tempRef ;
    DatabaseReference tempRef2 ;
     FirebaseAuth mAuth;
    Activity HomeActivity;
    HomeActivity HomeActivity2;
    User value;
    List<Products> products;
    List<String> listUser;
    myLists myList;
    String userID;
   private ModelFirebase()
    {
        database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();

    }
public User getUser()
{
    return value;
}
    public List<Products> getProducts()
    {
        return products;
    }

    public void newUser(String email)
    {
         myRef = database.getReference("Users");
        String userID = myRef.push().getKey();
        StringBuilder temp= new StringBuilder();
     String[] tempList =email.split("@");
     String point = tempList[1];
     String[] PointList= point.split("\\.");
     temp.append(tempList[0]);
        temp.append(PointList[0]);
        temp.append(PointList[1]);
        value= new User(email, temp.toString().trim() );
        myRef.child(temp.toString().trim()).setValue(value);


    }
    public void addUserToList(String userID,myLists list)
    {
        StringBuilder temp= new StringBuilder();
        String[] tempList =userID.split("@");
        String point = tempList[1];
        String[] PointList= point.split("\\.");
        temp.append(tempList[0]);
        temp.append(PointList[0]);
        temp.append(PointList[1]);
        myRef=database.getReference("Users");
        myRef.child(temp.toString().trim()).child("lists").child(list.listID).setValue(list);
        tempRef=database.getReference("AllLists");
        tempRef.child(list.listID).child("user").child(temp.toString().trim()).setValue(userID);
    }
    public void newList(String name,String Userid){

        myRef =database.getReference("Users");
        tempRef=database.getReference("AllLists");

        myRef=  myRef.child(Userid).child("lists");
        String listID = myRef.push().getKey();
        myLists myNewList =new myLists(name,listID);
        value.addtolist(myNewList);
        myRef.child(listID).setValue(myNewList);
        tempRef.child(listID).setValue(myNewList);
        tempRef.child(listID).child("user").child(userID).setValue(value.email);
    }
    public void newProducts(String name,String id){

        myRef=database.getReference("AllLists");
        Products Products =new Products(name,false);
        this.products.add(Products);
        myRef.child(id).child("Products").child(name).setValue(Products);

    }

    public void UpdateProducts(Products products, Boolean bool, String id)
    {
        myRef=database.getReference("AllLists");

        myRef.child(id).child("Products").child(products.name).setValue(products);


    }
    public FirebaseAuth getmAuth()
    {
        return mAuth;
    }
    public String getUserId(String userID){

        StringBuilder temp= new StringBuilder();
        String[] tempList =userID.split("@");
        String point = tempList[1];
        String[] PointList= point.split("\\.");
        temp.append(tempList[0]);
        temp.append(PointList[0]);
        temp.append(PointList[1]);
       return temp.toString().trim();

    }

    public void getUserData(String id, final Activity HomeActivity)
    {
        this.HomeActivity=HomeActivity;
         value=new User();

        myRef = database.getReference("Users");
         userID=id.trim();
        tempRef2 = database.getReference("AllLists");

        myRef = myRef.child(userID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                value = dataSnapshot.getValue(User.class);
                tempRef=myRef.child("lists");
                tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                            myList=(myLists) postSnapshot.getValue(myLists.class);

                            value.addtolist(myList);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(HomeActivity, "Data Received: "+ value.email, Toast.LENGTH_SHORT).show();

                Intent intent= new Intent(HomeActivity,HomeActivity.class);

                intent.putExtra("userID",userID);

                HomeActivity.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + databaseError.getMessage());

            }

        });


    }

    public void getListData(String id,HomeActivity HomeActivity){
        DatabaseReference myRef ;
     //   myList= new myLists();
       products= new ArrayList<>();
      // products.clear();
      this.HomeActivity2=HomeActivity;
       // final Object[] list = new Object[1];
        String userID=id.trim();
        tempRef2 = database.getReference("AllLists");
        tempRef2 = tempRef2.child(userID).child("Products");

        final Products[] pro = {new Products()};
        tempRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                     pro[0] =postSnapshot.getValue(Products.class);

                        products.add(pro[0]);


                }

                NavDirections directions = MylistFragmentDirections.actionGlobalProductsFragment();
                HomeActivity2.navCtrl.navigate(directions);
                Toast.makeText(HomeActivity2, "Data Received: "+ pro[0].name, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + error.getMessage());
            }
        });
        tempRef2.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                NavDirections directions = MylistFragmentDirections.actionGlobalProductsFragment();
                HomeActivity2.navCtrl.navigate(directions);
                Toast.makeText(HomeActivity2, "Data Received: "+ pro[0].name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
   }
   public void removeProducts(Products product, String id)
   {

       tempRef2=database.getReference("AllLists");

       this.products.remove(product);

       tempRef2.child(id).child("Products").child(product.name).removeValue();

   }
    public void removeList(myLists myLists)
    {

        myRef =database.getReference("Users");
        myRef =myRef.child(userID);

        myRef.child("lists").child(myLists.listID).removeValue();
        tempRef2.child(myLists.listID).removeValue();

    }
    public void getUserOfList(String listID) {

        listUser=new ArrayList<>();
        myRef = database.getReference("AllLists");
        myRef = myRef.child(listID).child("user");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    listUser.add(postSnapshot.getValue(String.class));
                }

                NavDirections directions = MylistFragmentDirections.actionGlobalAddListFragment();
                HomeActivity2.navCtrl.navigate(directions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + databaseError.getMessage());

            }


        });
    }
    public List<String> getUserList()
    {
        return listUser;
    }


}
