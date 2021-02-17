package com.example.fitshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitshare.model.ModelList;
import com.example.fitshare.model.ModelUser;
import com.example.fitshare.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {


    Button Sign_btn;
    EditText Email_edit;
    EditText Password_edit;
    String userID;
    Button Forgot_Password_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Sign_btn = findViewById(R.id.Sign_btn);
        Email_edit = findViewById(R.id.Email_edit);
        Password_edit = findViewById(R.id.Password_edit);
        Forgot_Password_btn = findViewById(R.id.Forgot_Password_btn);
        ModelUser.instance.setActivity(this);
        ModelUser.instance.IfUserAlreadyLoggedin();


        //Rest password mail
        Forgot_Password_btn.setOnClickListener(v -> {
            String email = Email_edit.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Email_edit.setError("email");
                return;
            }
            ModelUser.instance.ForgotPassword(email);

        });


        Sign_btn.setOnClickListener(v -> {

            Sign_btn.setEnabled(false);
            String email = Email_edit.getText().toString().trim();
            String Password = Password_edit.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Email_edit.setError("email");
                Sign_btn.setEnabled(true);
                return;
            }

            if (TextUtils.isEmpty(Password)) {
                Password_edit.setError("password");
                Sign_btn.setEnabled(true);
                return;
            }
            if (Password.length() <= 6) {
                Password_edit.setError("Password < 7");
                Sign_btn.setEnabled(true);
                return;
            }
            if (!email.contains("@")) {
                Email_edit.setError("Invalid email");
                Sign_btn.setEnabled(true);
                return;
            } else {
                String[] tempList = email.split("@");
                if (tempList.length < 2) {
                    Email_edit.setError("Invalid email");
                    Sign_btn.setEnabled(true);
                    return;
                } else if (!tempList[1].contains(".")) {
                    Email_edit.setError("Invalid email");
                    Sign_btn.setEnabled(true);
                    return;
                }

            }


            email = email.toLowerCase();

            ModelUser.instance.SignIn(email, Password);


        });

    }

}