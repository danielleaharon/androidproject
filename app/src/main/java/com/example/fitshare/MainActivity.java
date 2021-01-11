package com.example.fitshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitshare.model.ModelList;
import com.example.fitshare.model.ModelProduct;
import com.example.fitshare.model.ModelUser;
import com.example.fitshare.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button Sign_btn;
    EditText Email_edit;
    EditText Password_edit;
    String userID;
    Button Forgot_Password_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

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
            openUploadPage();
            userID = ModelUser.instance.cleanUserName(user.getEmail());
            ModelUser.instance.getUser(userID, new ModelUser.GetUserListener() {
                @Override
                public void onComplete() {
                    openLists();
                }
            });


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
                    Toast.makeText(MainActivity.this, email + ", welcome ", Toast.LENGTH_LONG).show();

                    String email1 = email.toLowerCase();
                    String userID = ModelUser.instance.cleanUserName(email);
                    User user = new User(email, userID, "Hebrew");
                    ModelUser.instance.newUser(user, new ModelUser.AddUserListener() {
                        @Override
                        public void onComplete() {
                            SignIn(email1, pass);
                        }
                    });


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

                    openUploadPage();
                    userID = ModelUser.instance.cleanUserName(email);
                    ModelUser.instance.getUser(userID, new ModelUser.GetUserListener() {
                        @Override
                        public void onComplete() {
                            openLists();
                        }
                    });


                } else {
                    task.getException().printStackTrace();
                    Register(email, pass);
                    Log.d("TAG", "Login failed ");

                }
            }
        });

    }

    public void openLists() {

        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
        this.finish();

    }

    public void openUploadPage() {
        hideKeyboard();
        UploadFragment UploadFragment = new UploadFragment();
        UploadFragment.parent = this;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main, UploadFragment, "TAG");
        transaction.addToBackStack("TAG");
        transaction.commit();
    }
}