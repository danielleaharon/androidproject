package com.example.fitshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitshare.model.ModelFirebase;
import com.example.fitshare.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    NavController navCtrl;
    private FirebaseAuth mAuth;
    Button Sign_btn;
    FirebaseUser currentUser;
     ModelFirebase  db ;
    EditText Email_edit;
    EditText Password_edit;
    String userID;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db= ModelFirebase.instance;
        mAuth = ModelFirebase.instance.getmAuth();

        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
//        navCtrl= Navigation.findNavController(this,R.id.home_nav_host);
//        NavigationUI.setupActionBarWithNavController(this,navCtrl);

        Sign_btn= findViewById(R.id.Sign_btn);
        Email_edit=findViewById(R.id.Email_edit);
        Password_edit=findViewById(R.id.Password_edit);



        Sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=Email_edit.getText().toString().trim();
                String  Password = Password_edit.getText().toString().trim();
                if(TextUtils.isEmpty(email))
                {
                    Email_edit.setError("need email");
                    return;
                }
                if(TextUtils.isEmpty(Password))
                {
                    Password_edit.setError("need pass");
                    return;
                }
                if(Password.length()<=6){
                    Password_edit.setError("need to be >=6");
                    return;
                }
                SignIn(email,Password);


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }



    void Register(final String email, final String pass)
    {

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "new user: "+email+"  " +pass);
                    Toast.makeText(MainActivity.this,"נוצר משתמש חדש, ברוכים הבאים ",Toast.LENGTH_LONG).show();

                    db.newUser(email);

                    SignIn(email,pass);

                } else {
                    task.getException().printStackTrace();
                    Toast.makeText(MainActivity.this,"המשתמש כבר קיים ",Toast.LENGTH_LONG).show();
                    Log.d("TAG", "Failed to create new user");

                }
            }
        });

    }
    void SignIn(final String email, final String pass)
    {

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Login successful ");
                    userID = db.getUserId(task.getResult().getUser().getUid());


                    db.getUserData(userID,MainActivity.this);

                    Log.d("TAG", userID);

//                    NavDirections directions = LoginFragmentDirections.actionGlobalMylistFragment();
//                    navCtrl.navigate(directions);

                } else {
                    task.getException().printStackTrace();
                    Register(email,pass);

                  //  Toast.makeText(MainActivity.this,"לא קיים משתמש ",Toast.LENGTH_LONG).show();
                    Log.d("TAG", "Login failed ");

                }
            }
        });

    }

}