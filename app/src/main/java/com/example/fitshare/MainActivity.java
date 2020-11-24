package com.example.fitshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    NavController navCtrl;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
Button btn= findViewById(R.id.Register_btn);
        mAuth = FirebaseAuth.getInstance();



btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mAuth.createUserWithEmailAndPassword("danielle222@gmail.com", "1111111111111").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "new usr: ");
                    FirebaseUser user = mAuth.getCurrentUser(); //You Firebase user

                } else {
                    task.getException().printStackTrace();

                    Log.d("TAG", "no usr ");

                }
            }
        });
    }
});

//        navCtrl= Navigation.findNavController(this,R.id.home_nav_host);
//        NavigationUI.setupActionBarWithNavController(this,navCtrl);


    }

    void OpenRegisterFragment()
    {
//        navCtrl= Navigation.findNavController(this,R.id.home_nav_host);
//        NavDirections directions = LoginFragmentDirections.actionGlobalRegisterFragment();
//        navCtrl.navigate(directions);

    }
    void Register()
    {
        mAuth.createUserWithEmailAndPassword("danielle@gmail.com","12345").addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    Log.d("TAG","new usr: " );
                else Log.d("TAG","no usr " );

            }
        });
    }
}