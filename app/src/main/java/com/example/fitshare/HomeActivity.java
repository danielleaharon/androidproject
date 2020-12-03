package com.example.fitshare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.fitshare.model.ModelFirebase;
import com.example.fitshare.model.Products;
import com.example.fitshare.model.User;
import com.example.fitshare.model.myLists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public NavController navCtrl;
    DatabaseReference myRef;
    FirebaseUser currentUser;
    public FirebaseAuth mAuth;
     ModelFirebase db;
    User value;
    String listID;
    String userID;
   public List<Products> myUserProductsLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db =ModelFirebase.instance;

        Intent intent = getIntent();
         userID =  intent.getStringExtra("userID").toString();

       value= ModelFirebase.instance.getUser();


        navCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        NavigationUI.setupActionBarWithNavController(this, navCtrl);




    }
    public void openList(String id,int position)
    {
        listID=id;
        ModelFirebase.instance.getListData(id,this);



    }


}