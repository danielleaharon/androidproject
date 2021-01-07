package com.example.fitshare;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.fitshare.model.ModelListDao;
import com.example.fitshare.model.ModelUserDao;
import com.example.fitshare.model.Products;
import com.example.fitshare.model.User;
import com.example.fitshare.model.myLists;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    public NavController navCtrl;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int GALLERY_REQUEST = 2;

    User value;
    String listID = null;
    String ListName;


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

    List<myLists> AllMyLists= new ArrayList<>();
    FloatingActionButton floating_addList_btn;


    //Dialog





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        titel = findViewById(R.id.title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

      //  db = ModelFirebase.instance;
        value = ModelFirebase.instance.getUser();
        language_btn = findViewById(R.id.language_btn);
        language_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopup(v);


            }
        });

//        ModelListDao.instance.getAllMyLists(value.getId(),new ModelListDao.GetAllMyListsListener() {
//            @Override
//            public void onComplete(List<myLists> data) {
//                AllMyLists=data;
//            }
//        });

        ModelFirebase.instance.createAllUserList();
        floating_addList_btn = findViewById(R.id.floating_addList_btn);
        floating_addList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                userDailog = new ArrayList<>();
                userDailog.add(value.getEmail());
                NavDirections directions = MylistFragmentDirections.actionMylistFragmentToAddListFragment();
                navCtrl.navigate(directions);


            }
        });

        logout_btn = findViewById(R.id.logout_btn);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelFirebase.instance.Logout();
            }
        });
        addList_btn = findViewById(R.id.add_btn);
        save_btn = findViewById(R.id.save_btn);

        addList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listID != null)
                    ModelFirebase.instance.getUserOfList(listID);
                else {
                    userDailog = new ArrayList<>();
                    userDailog.add(value.getEmail());
                    NavDirections directions = MylistFragmentDirections.actionGlobalAddListFragment();
                    navCtrl.navigate(directions);
                }
            }
        });
        copy_list_btn = findViewById(R.id.copy_list_btn);
        copy_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        navCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        NavigationUI.setupActionBarWithNavController(this, navCtrl);


    }

    public void OpenProductList() {

        NavDirections directions = MylistFragmentDirections.actionMylistFragmentToProductsFragment();
        navCtrl.navigate(directions);
    }

    public void openList(String ListId, String listName, int position) {
        this.listID = ListId;
        this.ListName = listName;
        this.ListPosition = position;
        ModelListDao.instance.getAllMyLists(value.getId(), new ModelListDao.GetAllMyListsListener() {
            @Override
            public void onComplete(List<myLists> data) {
                AllMyLists=data;
                CorrectList = AllMyLists.get(position);
            }
        });

        ModelFirebase.instance.getListData(ListId, this, position);


    }

//    public void RefreshList() {
//        myLists myLists = db.getUser().getMyLists().get(this.ListPosition);
//        this.ListName = myLists.getListName();
//
//
//
//    }


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

        if (item.getItemId() == android.R.id.home) {
            navCtrl.navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openImage(Products products, String listID, String url) {


        Products product;
        ScaleGestureDetector scaleGestureDetector;


        AddListAdapter addListAdapter;
        FloatingActionButton add_img;

        product = products;
        Dialog dialog1 = new Dialog(this);

        dialog1.setContentView(R.layout.camar_dailog);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        Window window = dialog1.getWindow();
        window.setGravity(Gravity.CENTER);


        imageDialog = dialog1.findViewById(R.id.imageDailog);
        if (!product.getImgUrl().equals("noImage"))
            Picasso.get().load(product.getImgUrl()).into(imageDialog);
        else imageDialog.setImageResource(R.drawable.nophoto);


        add_img = dialog1.findViewById(R.id.addImg_btn);
        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button camra_btn;
                Button gallery_btn;
                Dialog dialog2 = new Dialog(HomeActivity.this);
                dialog2.setContentView(R.layout.gallery_or_camera_dialog);
                dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Window window = dialog2.getWindow();
                window.setGravity(Gravity.CENTER);


                camra_btn=dialog2.findViewById(R.id.camra_btn);
                gallery_btn=dialog2.findViewById(R.id.gallery_btn);

                if(value.getLanguage().equals("English"))
                {
                    camra_btn.setText("Open a camera");
                    gallery_btn.setText("Gallery");

                }
                camra_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhotoCamera();
                        dialog2.cancel();
                    }
                });

                gallery_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhotoGallery();
                        dialog2.cancel();

                    }
                });
                dialog2.setCancelable(true);
                window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                dialog2.show();


            }
        });


        Button save_btn = dialog1.findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageBitmap != null) {
                    ImageModel.uploadImage(imageBitmap, product.getName() + listID, new ImageModel.Listener() {
                        @Override
                        public void onSuccess(String url) {


                            product.setImgUrl(url);
                            ModelFirebase.instance.UpdateProducts(product, listID);
                            dialog1.cancel();


                        }

                        @Override
                        public void onFail() {
                            dialog1.cancel();

                        }
                    });
                } else dialog1.cancel();
            }

        });

        Button cancel_btn = dialog1.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });


        dialog1.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog1.show();


    }
    public static final int PICK_IMAGE = 1;

    public void takePhotoCamera() {


        Intent tackPicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (tackPicIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(tackPicIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    public void takePhotoGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {



        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        imageBitmap = BitmapFactory.decodeStream(imageStream);
                        imageDialog.setImageBitmap(imageBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                        Bundle extras = data.getExtras();
                        imageBitmap = (Bitmap) extras.get("data");
                        imageDialog.setImageBitmap(imageBitmap);

                    }
                    break;

            }
        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.hebrew:
                value.setLanguage( "Hebrew");
                ModelFirebase.instance.changeLanguage("Hebrew");
                setTitle("רשימות");

                break;
            case R.id.english:
                value.setLanguage("English");
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

    public String  getListID() {

      return   this.listID;

    }
    public void RefreshList()
    {
        ModelListDao.instance.getAllMyLists(value.getId(), new ModelListDao.GetAllMyListsListener() {
            @Override
            public void onComplete(List<myLists> data) {
                AllMyLists=data;
            }
        });
    }
    public int getPosition(myLists myLists1)
    {
        int i=0;


        for (myLists my: AllMyLists) {
            if(my.getListID().equals(myLists1.getListID())) {
                return i;
            }
            i++;
        }
        return -1;
    }


}
