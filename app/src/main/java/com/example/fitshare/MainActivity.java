package com.example.fitshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
    ModelFirebase db;
    EditText Email_edit;
    EditText Password_edit;
    String userID;
    DatabaseReference myRef;
    Button Forgot_Password_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = ModelFirebase.instance;
        mAuth = ModelFirebase.instance.getmAuth();

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Sign_btn = findViewById(R.id.Sign_btn);
        Email_edit = findViewById(R.id.Email_edit);
        Password_edit = findViewById(R.id.Password_edit);
        Forgot_Password_btn = findViewById(R.id.Forgot_Password_btn);

        //If the user has already logged in to the app, doesn't ask them to reconnect
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            db.getUserData(user.getEmail(), MainActivity.this);
        } else {
            // User is signed out

        }


        //Rest password mail
        Forgot_Password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email_edit.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Email_edit.setError("email");
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", "Email sent.");
                                }
                            }
                        });
            }
        });


        Sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Email_edit.getText().toString().trim();
                String Password = Password_edit.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Email_edit.setError("email");
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    Password_edit.setError("password");
                    return;
                }
                if (Password.length() <= 6) {
                    Password_edit.setError("Password < 7");
                    return;
                }
                if(!email.contains("@")) {
                        Email_edit.setError("Invalid email");
                    return;
                }
                else {
                    String[] tempList = email.split("@");
                    if(tempList.length<2) {
                        Email_edit.setError("Invalid email");
                        return;
                    }
                    else if(!tempList[1].contains(".")) {
                        Email_edit.setError("Invalid email");
                        return;
                    }

                }


                email=email.toLowerCase();
                SignIn(email, Password);


            }
        });

    }

    public void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

    }


    void Register(final String email, final String pass) {

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "new user: " + email + "  " + pass);
                    Toast.makeText(MainActivity.this, email+", welcome ", Toast.LENGTH_LONG).show();


                    String email1=email.toLowerCase();
                    db.newUser(email1);

                    SignIn(email1, pass);

                } else {
                    task.getException().printStackTrace();
                    Toast.makeText(MainActivity.this, "הסיסמא לא נכונה ", Toast.LENGTH_LONG).show();
                    Log.d("TAG", "Failed to create new user");

                }
            }
        });

    }

    void SignIn(final String email, final String pass) {

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Log.d("TAG", "Login successful ");

                    //openUploadPage();
                    userID = db.getUserId(email);
                    db.getUserData(email, MainActivity.this);


                } else {
                    task.getException().printStackTrace();
                    Register(email, pass);

                    //  Toast.makeText(MainActivity.this,"לא קיים משתמש ",Toast.LENGTH_LONG).show();
                    Log.d("TAG", "Login failed ");

                }
            }
        });

    }

    public void openUploadPage() {
        UploadFragment UploadFragment = new UploadFragment();
        UploadFragment.parent = this;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main, UploadFragment, "TAG");
        transaction.addToBackStack("TAG");
        transaction.commit();
    }
}