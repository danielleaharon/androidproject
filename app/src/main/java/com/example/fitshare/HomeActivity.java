package com.example.fitshare;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.fitshare.model.ImageModel;

import com.example.fitshare.model.ModelList;
import com.example.fitshare.model.ModelProduct;
import com.example.fitshare.model.ModelUser;
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

    User user;
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
    List<myLists> AllMyLists = new ArrayList<>();
    FloatingActionButton floating_addList_btn;
    int color;
    Toolbar toolbar;
    Button colorbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        titel = findViewById(R.id.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        user = ModelUser.instance.getUser();
        language_btn = findViewById(R.id.language_btn);
        language_btn.setOnClickListener((v) -> showPopup(v));

        copy_list_btn = findViewById(R.id.copy_list_btn);
        color = user.getColor();
        toolbar.setBackgroundColor(color);
        colorbtn = findViewById(R.id.colorbtn);
        colorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopup2(v);
            }
        });


        floating_addList_btn = findViewById(R.id.floating_addList_btn);
        floating_addList_btn.setBackgroundTintList(ColorStateList.valueOf(color));
        floating_addList_btn.setOnClickListener(view -> {

            userDailog = new ArrayList<>();
            userDailog.add(user.getEmail());
            NavDirections directions = MylistFragmentDirections.actionMylistFragmentToAddListFragment();
            navCtrl.navigate(directions);


        });

        logout_btn = findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener((v) -> ModelUser.instance.Logout(this));


        addList_btn = findViewById(R.id.add_btn);
        save_btn = findViewById(R.id.save_btn);

        addList_btn.setOnClickListener((v) -> {

            if (listID != null) {
                ModelList.instance.getUserList(listID, data -> {
                    userDailog = data;
                    NavDirections directions = MylistFragmentDirections.actionGlobalAddListFragment();
                    navCtrl.navigate(directions);
                });
            } else {
                userDailog = new ArrayList<>();
                userDailog.add(user.getEmail());
                NavDirections directions = MylistFragmentDirections.actionGlobalAddListFragment();
                navCtrl.navigate(directions);
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
        CorrectList = AllMyLists.get(position);

        ModelProduct.instance.RefreshProducts(listID, () -> OpenProductList());
    }

    public void setTitle(String titel) {
        this.titel.setText(titel);
    }

    public void addToUserDailog(String userEamil) {
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


                camra_btn = dialog2.findViewById(R.id.camra_btn);
                gallery_btn = dialog2.findViewById(R.id.gallery_btn);

                if (user.getLanguage().equals("English")) {
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
                            ModelProduct.instance.updateProducts(products, () -> dialog1.cancel());

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
        cancel_btn.setOnClickListener(v -> dialog1.cancel());

        dialog1.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog1.show();

    }

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
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.hebrew:
                user.setLanguage("Hebrew");
                ModelUser.instance.UpdateUserLanguage(user, null);
                setTitle("רשימות");

                break;
            case R.id.english:
                user.setLanguage("English");
                ModelUser.instance.UpdateUserLanguage(user, null);
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

    @SuppressLint("RestrictedApi")
    public void showPopup2(View view) {


        PopupMenu menu = new PopupMenu(this, view);
        if (user.getLanguage().equals("English"))
            menu.inflate(R.menu.color_eng);
        else menu.inflate(R.menu.color);


        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.color1:
                        color = Color.rgb(243, 214, 104);
                        user.setColor(color);
                        ModelUser.instance.UpdateUserColor(user, new ModelUser.AddUserListener() {
                            @Override
                            public void onComplete() {
                                toolbar.setBackgroundColor(color);
                                floating_addList_btn.setBackgroundTintList(ColorStateList.valueOf(color));
                            }
                        });


                        break;
                    case R.id.colo2:
                        color = Color.rgb(152, 230, 152);
                        user.setColor(color);
                        ModelUser.instance.UpdateUserColor(user, new ModelUser.AddUserListener() {
                            @Override
                            public void onComplete() {
                                toolbar.setBackgroundColor(color);
                                floating_addList_btn.setBackgroundTintList(ColorStateList.valueOf(color));
                            }
                        });
                        break;
                    case R.id.color3:
                        color = Color.rgb(153, 230, 255);
                        user.setColor(color);
                        ModelUser.instance.UpdateUserColor(user, new ModelUser.AddUserListener() {
                            @Override
                            public void onComplete() {
                                toolbar.setBackgroundColor(color);
                                floating_addList_btn.setBackgroundTintList(ColorStateList.valueOf(color));
                            }
                        });
                        break;
                    case R.id.color4:
                        color = Color.rgb(255, 204, 102);
                        user.setColor(color);

                        ModelUser.instance.UpdateUserColor(user, new ModelUser.AddUserListener() {
                            @Override
                            public void onComplete() {
                                toolbar.setBackgroundColor(color);
                                floating_addList_btn.setBackgroundTintList(ColorStateList.valueOf(color));
                            }
                        });
                        break;
                    default:

                        return false;
                }
                navCtrl.popBackStack();
                NavDirections directions = MylistFragmentDirections.actionGlobalMylistFragment();
                navCtrl.navigate(directions);
                return true;
            }
        });
        MenuPopupHelper menuHelper = new MenuPopupHelper(this, (MenuBuilder) menu.getMenu(), view);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();
    }

    public String getListID() {

        return this.listID;

    }

    public int getListPosition(myLists myLists1) {
        int i = 0;

        for (myLists my : AllMyLists) {
            if (my.getListID().equals(myLists1.getListID())) {
                return i;
            }
            i++;
        }
        return -1;
    }

//    public void Logout() {
//        ModelUser.instance.Logout(this);
//    }
}
