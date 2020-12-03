package com.example.fitshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.fitshare.model.ModelFirebase;
import com.example.fitshare.model.Products;
import com.example.fitshare.model.User;
import com.example.fitshare.model.myLists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public NavController navCtrl;
    DatabaseReference myRef;
    FirebaseUser currentUser;
    public FirebaseAuth mAuth;
     ModelFirebase db;
    User value;
    String listID=null;
    String ListName;
    String userID;
    Button addUserList_btn;
    Button addList_btn;
    myLists CorrectList;
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

        addUserList_btn=findViewById(R.id.add_user);
        addUserList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        addList_btn=findViewById(R.id.add_btn);

        addList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listID!=null)
                ModelFirebase.instance.getUserOfList(listID);
            }
        });

        navCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        NavigationUI.setupActionBarWithNavController(this, navCtrl);




    }
    public void openList(String ListId,String listName)
    {
        listID=ListId;
        this.ListName=listName;
        CorrectList =new myLists(listName,listID);
        ModelFirebase.instance.getListData(ListId,this);



    }
    public void openDialog()
    {

    addUser_Dialog dialog = new addUser_Dialog();
    dialog.show(getSupportFragmentManager(),"add user");
   }
   public void addUserToList(String Userid)
   {

       ModelFirebase.instance.addUserToList(Userid,CorrectList);
   }

}