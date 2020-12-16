package com.example.fitshare;

import android.animation.Animator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.fitshare.model.ImageModel;
import com.example.fitshare.model.ModelFirebase;
import com.example.fitshare.model.Products;
import com.example.fitshare.model.User;
import com.example.fitshare.model.myLists;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    public NavController navCtrl;
    DatabaseReference myRef;
    FirebaseUser currentUser;
    public FirebaseAuth mAuth;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ModelFirebase db;
    User value;
    String listID = null;
    String ListName;
    String userID;
    Button addUserList_btn;
    Button addList_btn;
    myLists CorrectList;
    TextView titel;
    int ListPosition;
    Button logout_btn;
    Button save_btn;
    Bitmap imageBitmap;
    ImageView imageDialog;
    Button copy_list_btn;
    Button language_btn;
    List<String> userDailog = new ArrayList<>();
    public List<Products> myUserProductsLists;
    FloatingActionButton floating_addList_btn;



    //Dialog
    private float mScaleFactor = 1.0f;


    int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        titel = findViewById(R.id.title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        db = ModelFirebase.instance;

        language_btn=findViewById(R.id.language_btn);
        language_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopup(v);


            }
        });
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID").toString();

        value = ModelFirebase.instance.getUser();
       ModelFirebase.instance.createAllUserList();
         floating_addList_btn = findViewById(R.id.floating_addList_btn);
         floating_addList_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {


                     userDailog = new ArrayList<>();
                     userDailog.add(value.email);
                     NavDirections directions = MylistFragmentDirections.actionGlobalAddListFragment();
                     navCtrl.navigate(directions);


             }
         });

        logout_btn=findViewById(R.id.logout_btn);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelFirebase.instance.Logout();
            }
        });
        addList_btn = findViewById(R.id.add_btn);
        save_btn=findViewById(R.id.save_btn);

        addList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listID != null)
                    ModelFirebase.instance.getUserOfList(listID);
                else {
                    userDailog = new ArrayList<>();
                    userDailog.add(value.email);
                    NavDirections directions = MylistFragmentDirections.actionGlobalAddListFragment();
                    navCtrl.navigate(directions);
                }
            }
        });
        copy_list_btn=findViewById(R.id.copy_list_btn);
        copy_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        navCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        NavigationUI.setupActionBarWithNavController(this, navCtrl);


    }
    public void OpenProductList( )
    {

        NavDirections directions = MylistFragmentDirections.actionGlobalProductsFragment();
        navCtrl.navigate(directions);
    }

    public void openList(String ListId, String listName, int position) {
        listID = ListId;
        this.ListName = listName;
        this.ListPosition = position;
        CorrectList =db.getUser().getMyLists().get(position);
        ModelFirebase.instance.getListData(ListId, this,position);


    }

    public void RefreshList() {
        myLists myLists = db.getUser().getMyLists().get(this.ListPosition);
        this.ListName = myLists.ListName;

        //    ModelFirebase.instance.getListData(ListId,this);


    }



    public void addUserToList(String Userid) {

        ModelFirebase.instance.addUserToList(Userid, CorrectList);
    }

    public void setTitle(String titel) {
        this.titel.setText(titel);
    }

    public void setUserDailog(String userEamil) {
        userDailog.add(userEamil);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home) {
            navCtrl.navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

public void openImage(Products products,String listID,String url)
{


    Products product;
     ScaleGestureDetector scaleGestureDetector;


    AddListAdapter addListAdapter;
    FloatingActionButton add_img;

    product=products;
    Dialog dialog1=new Dialog(this);

    dialog1.setContentView(R.layout.camar_dailog);
    dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    Window window = dialog1.getWindow();
    window.setGravity(Gravity.CENTER);



    imageDialog = dialog1.findViewById(R.id.imageDailog);
    if(!product.imgUrl.equals("noImage"))
        Picasso.get().load(product.imgUrl).into(imageDialog);
    else imageDialog.setImageResource(R.drawable.nophoto);



    add_img = dialog1.findViewById(R.id.addImg_btn);
    add_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            takePhoto();

        }
    });



    Button save_btn=dialog1.findViewById(R.id.save_btn);
    save_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ImageModel.uploadImage(imageBitmap, product.name + listID, new ImageModel.Listener() {
                @Override
                public void onSuccess(String url) {

                    product.setImage(url);
                    ModelFirebase.instance.UpdateProducts(product,  listID);
                    dialog1.cancel();


                }

                @Override
                public void onFail() {

                }
            });
        }
    });

    Button cancel_btn=dialog1.findViewById(R.id.cancel_btn);
    cancel_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog1.cancel();
        }
    });



    dialog1.setCancelable(true);
    window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
    dialog1.show();





}
    public void takePhoto() {

        Intent tackPicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (tackPicIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(tackPicIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageDialog.setImageBitmap(imageBitmap);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.hebrew:
                    value.language = "Hebrew";
                    ModelFirebase.instance.changeLanguage("Hebrew");
                setTitle("רשימות");

                break;
            case R.id.english:
                value.language = "English";
                ModelFirebase.instance.changeLanguage("English");
                setTitle("Lists");
                break;

            default:

                return false;
        }
        navCtrl.popBackStack();
        NavDirections directions = MylistFragmentDirections.actionGlobalMylistFragment();
        navCtrl.navigate(directions);
        return true;
    }
    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);

            popup.inflate(R.menu.language_english);

        popup.show();
    }
}
