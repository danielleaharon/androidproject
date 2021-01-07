package com.example.fitshare.model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;

import com.example.fitshare.HomeActivity;
import com.example.fitshare.MainActivity;
import com.example.fitshare.MylistFragmentDirections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ModelFirebase {

    public static final ModelFirebase instance = new ModelFirebase();

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference tempRef;
    DatabaseReference tempRef2;
    FirebaseAuth mAuth;
    HomeActivity HomeActivity;
    int listPosition;
    User user;
    List<Products> productsList;
    List<Products> CopyproductsList;
    List<String> listUser;
    myLists myList;
    String userID;
    List<String> AllUsers = new ArrayList<>();

    private ModelFirebase() {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    public User getUser() {
        return user;
    }

    public List<Products> getProductsList() {
        return productsList;
    }

    public List<Products> getCopyproductsList() {
        return CopyproductsList;
    }


    public void newUser(String email) {


        myRef = database.getReference("Users");
        String userID = cleanUserName(email);
        user = new User(email, userID, "Hebrew");
        ModelUserDao.instance.addUser(user, new ModelUserDao.AddUserListener() {
            @Override
            public void onComplete() {

            }
        });
        myRef.child(userID).setValue(user);


    }

    public void addUserToList(String email, myLists list) {
        String userID = cleanUserName(email);
        myRef = database.getReference("Users");
        myRef.child(userID).child("lists").child(list.getListID()).setValue(list);
        tempRef = database.getReference("AllLists");
        tempRef.child(list.getListID()).child("user").child(userID).setValue(email);
    }

    public void newList(String name, String Userid, List<String> user) {

        DatabaseReference myRef;
        DatabaseReference tempRef;
        myRef = database.getReference("Users");
        tempRef = database.getReference("AllLists");

        myRef = myRef.child(Userid).child("lists");
        String listID = myRef.push().getKey();
        myLists myNewList = new myLists(name, listID, 0);
        myNewList.setUser(Userid);
        ModelListDao.instance.addMyList(myNewList, new ModelListDao.AddMyListsListener() {
            @Override
            public void onComplete() {
                Log.d("TAG", "ModelListDao  newList onComplete ");

                tempRef.child(listID).setValue(myNewList);
                for (String string : user) {
                    addUserToList(string, myNewList);
                }
            }
        });


    }

    public void newProducts(Products Products, String ListID, int listPosition) {
        DatabaseReference myRef;
        myRef = database.getReference("AllLists");
        tempRef = database.getReference("Users");

        ModelProductDao.instance.addProducts(Products, new ModelProductDao.AddProductsListener() {
            @Override
            public void onComplete() {
                productsList.add(Products);
                Log.d("TAG", "ModelProductDao addProducts OK ");
            }
        });


//        ModelListDao.instance.getAllMyLists(user.getId() {
//            @Override
//            public void onComplete(List<myLists> data) {
//                myLists myLists = data.get(listPosition);
//                myLists.setListCount(productsList.size());
//                ModelListDao.instance.updateMyList(myLists, new ModelListDao.updateMyListsListener() {
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//            }
//        });
        for (String userID : listUser) {
            userID = cleanUserName(userID);
            tempRef.child(userID).child("lists").child(ListID).child("listCount").setValue(productsList.size());

        }
        // this.user.getMyLists().get(listPosition).setListCount( productsList.size());
        //    tempRef.child(getUserId(user.email)).child("lists").child(id).child("listCount").setValue(productsList.size());
        myRef.child(ListID).child("listCount").setValue(productsList.size());
        myRef.child(ListID).child("Products").child(Products.getName()).setValue(Products);

    }


    public void UpdateProducts(Products products, String listID) {
        ModelProductDao.instance.updateProducts(products, new ModelListDao.updateMyListsListener() {
            @Override
            public void onComplete() {
                DatabaseReference myRef;
                myRef = database.getReference("AllLists");
                myRef.child(listID).child("Products").child(products.getName()).setValue(products);
            }
        });


    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public String getUserId(String email) {

        String userID = cleanUserName(email);

        return userID;

    }

    public void getUserData(String id, final MainActivity MainActivity) {

        MainActivity.hideKeyboard();
        MainActivity.openUploadPage();


        myRef = database.getReference("Users");
        userID = cleanUserName(id);

        ModelUserDao.instance.getAllUser(userID, new ModelUserDao.GetAllUserListener() {
            @Override
            public void onComplete(List<User> data) {
                if (data.size() != 0)
                    user = data.get(0);
//                user.setEmail(user.getId());
//                user.setId(cleanUserName(user.getId()));
            }
        });


        tempRef2 = database.getReference("AllLists");

        myRef = myRef.child(userID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (user == null) {
                    user = dataSnapshot.getValue(User.class);
                    ModelUserDao.instance.addUser(user, new ModelUserDao.AddUserListener() {
                        @Override
                        public void onComplete() {

                        }
                    });
                }
                //     user = dataSnapshot.getValue(User.class);
                tempRef = myRef.child("lists");
                tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            myList = (myLists) postSnapshot.getValue(myLists.class);
                            myList.setUser(user.getId());
                            ModelListDao.instance.addMyList(myList, new ModelListDao.AddMyListsListener() {
                                @Override
                                public void onComplete() {
                                    //   user.addtolist(myList);
                                    Intent intent = new Intent(MainActivity, HomeActivity.class);

                                    //                    intent.putExtra("userID", userID);

                                    MainActivity.startActivity(intent);
                                    MainActivity.finish();

                                }
                            });

                        }

//                        Intent intent = new Intent(MainActivity, HomeActivity.class);
//
//    //                    intent.putExtra("userID", userID);
//
//                        MainActivity.startActivity(intent);
//                        MainActivity.finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + databaseError.getMessage());

            }

        });
        myRef = database.getReference("Users");
        myRef = myRef.child(userID);
        tempRef = myRef.child("lists");
        tempRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myList = (myLists) snapshot.getValue(myLists.class);

                ModelListDao.instance.addMyList(myList, new ModelListDao.AddMyListsListener() {
                    @Override
                    public void onComplete() {
                        //user.addtolist(myList);
                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                myList = (myLists) snapshot.getValue(myLists.class);

                ModelListDao.instance.deleteMyList(myList, new ModelListDao.deleteMyListsListener() {
                    @Override
                    public void onComplete() {

                    }
                });

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void getListData(String id, final HomeActivity HomeActivity, int listPosition) {


        this.listPosition = listPosition;
        DatabaseReference tempRef2;
        DatabaseReference tempRef;
        productsList = new ArrayList<>();
        this.HomeActivity = HomeActivity;
        String listID = id.trim();
        listUser = new ArrayList<>();
        tempRef2 = database.getReference("AllLists");
        tempRef = tempRef2.child(listID).child("user");
        tempRef2 = tempRef2.child(listID).child("Products");

        tempRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Products products = postSnapshot.getValue(Products.class);

                    ModelProductDao.instance.addProducts(products, new ModelProductDao.AddProductsListener() {
                        @Override
                        public void onComplete() {

                            productsList.add(products);
                        }
                    });


                }

                tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            String user = postSnapshot.getValue(String.class);
                            listUser.add(user);
                        }
                        HomeActivity.OpenProductList();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + error.getMessage());
            }
        });

        tempRef2.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                HomeActivity.navCtrl.popBackStack();
                NavDirections directions = MylistFragmentDirections.actionGlobalProductsFragment();
                HomeActivity.navCtrl.navigate(directions);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getCopyListData(String id, final HomeActivity HomeActivity, int listPosition) {


        this.listPosition = listPosition;
        DatabaseReference tempRef2;
        DatabaseReference tempRef;
        CopyproductsList = new ArrayList<>();
        this.HomeActivity = HomeActivity;
        String listID = id.trim();

        tempRef2 = database.getReference("AllLists");

        tempRef2 = tempRef2.child(listID).child("Products");

        final Products[] pro = {new Products()};
        tempRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    pro[0] = postSnapshot.getValue(Products.class);
                    CopyproductsList.add(pro[0]);


                }

                CopyProduct(listID, listPosition);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + error.getMessage());
            }
        });

    }

    public void removeProducts(Products product, String id, int Position) {

        tempRef2 = database.getReference("AllLists");
        tempRef = database.getReference("Users");

        productsList.remove(Position);
        ModelProductDao.instance.deleteProducts(product, new ModelProductDao.deleteProductsListener() {
            @Override
            public void onComplete() {
                ModelListDao.instance.getAllMyLists(getUserId(user.getEmail()), new ModelListDao.GetAllMyListsListener() {
                    @Override
                    public void onComplete(List<myLists> data) {
                        myLists myLists = data.get(listPosition);
                        myLists.setListCount(productsList.size());
                        ModelListDao.instance.addMyList(myLists, new ModelListDao.AddMyListsListener() {

                            @Override
                            public void onComplete() {

                            }
                        });
                        tempRef.child(getUserId(user.getEmail())).child("lists").child(id).child("listCount").setValue(productsList.size());
                        tempRef2.child(id).child("listCount").setValue(productsList.size());
                        tempRef2.child(id).child("Products").child(product.getName()).removeValue();
                        if (!product.getImgUrl().equals("noImage"))
                            ImageModel.deleteImage(product.getImgUrl());

                    }

                });
                // user.getMyLists().get(listPosition).setListCount( productsList.size());
                tempRef.child(getUserId(user.getEmail())).child("lists").child(id).child("listCount").setValue(productsList.size());
                tempRef2.child(id).child("listCount").setValue(productsList.size());
                tempRef2.child(id).child("Products").child(product.getName()).removeValue();
                if (!product.getImgUrl().equals("noImage"))
                    ImageModel.deleteImage(product.getImgUrl());
            }
        });

    }

    public void removeList(final myLists myLists) {


        ModelListDao.instance.deleteMyList(myLists, new ModelListDao.deleteMyListsListener() {
            @Override
            public void onComplete() {
                myRef = database.getReference("Users");
                tempRef2 = database.getReference("AllLists");

                tempRef2 = tempRef2.child(myLists.getListID()).child("user");

                tempRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            String id = (postSnapshot.getValue(String.class));
                            id = cleanUserName(id);
                            myRef.child(id).child("lists").child(myLists.getListID()).removeValue();
                        }

                        tempRef = database.getReference("AllLists");
                        tempRef.child(myLists.getListID()).removeValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
//        myRef = database.getReference("Users");
//        tempRef2 = database.getReference("AllLists");
//
//        tempRef2 = tempRef2.child(myLists.getListID()).child("user");
//
//        tempRef2.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//
//                    String id = (postSnapshot.getValue(String.class));
//                    id = cleanUserName(id);
//                    myRef.child(id).child("lists").child(myLists.getListID()).removeValue();
//                }
//
//                tempRef = database.getReference("AllLists");
//                tempRef.child(myLists.getListID()).removeValue();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

    public void getUserOfList(String listID) {
        DatabaseReference myRef;
        DatabaseReference tempRef;
        listUser = new ArrayList<>();
        myRef = database.getReference("AllLists");
        myRef = myRef.child(listID).child("user");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    listUser.add(postSnapshot.getValue(String.class));
                }

                NavDirections directions = MylistFragmentDirections.actionGlobalAddListFragment();
                HomeActivity.navCtrl.navigate(directions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + databaseError.getMessage());

            }


        });
    }

    public List<String> getUserList() {
        return listUser;
    }

    public void UpdateListData(myLists myList, int position) {

        myRef = database.getReference("Users");
        tempRef2 = database.getReference("Users");
        tempRef = database.getReference("AllLists");

        for (String userMail : listUser) {
            String temp = cleanUserName(userMail);


            myRef.child(temp).child("lists").child(myList.getListID()).child("ListName").setValue(myList.getListName());
        }

        ModelListDao.instance.updateMyList(myList, new ModelListDao.updateMyListsListener() {
            @Override
            public void onComplete() {
                tempRef.child(myList.getListID()).child("ListName").setValue(myList.getListName());
            }
        });


    }

    public String cleanUserName(String email) {
        StringBuilder temp = new StringBuilder();
        String[] tempList = email.split("@");
        String[] t = null;
        if (tempList[0].contains("."))
            t = tempList[0].split("\\.");
        else if (tempList[0].contains("_"))
            t = tempList[0].split("_");
        else temp.append(tempList[0]);


        String[] PointList = null;

        if (t != null)
            for (String s : t) {
                temp.append(s);
            }

        if (tempList.length >= 2) {
            PointList = tempList[1].split("\\.");
            temp.append(PointList[0]);
            temp.append(PointList[1]);
        }
        return temp.toString().trim();
    }

    public void refreshProductList(String listID) {
        tempRef2 = database.getReference("AllLists");
        tempRef2 = tempRef2.child(listID).child("Products");
        productsList = new ArrayList<>();

        tempRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Products products = postSnapshot.getValue(Products.class);
                    ModelProductDao.instance.addProducts(products, new ModelProductDao.AddProductsListener() {
                        @Override
                        public void onComplete() {
                            productsList.add(products);
                        }
                    });
                    // productsList.add(products);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void refreshUserList() {
        myRef = database.getReference("Users");


        myRef = myRef.child(userID).child("lists");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //    user.myLists = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    myList = (myLists) postSnapshot.getValue(myLists.class);

                    ModelListDao.instance.addMyList(myList, new ModelListDao.AddMyListsListener() {
                        @Override
                        public void onComplete() {
                            //user.addtolist(myList);
                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void createAllUserList() {
        DatabaseReference myRef;
        DatabaseReference tempRef;
        myRef = database.getReference("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    User user = postSnapshot.getValue(User.class);
                    AllUsers.add(user.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public Boolean CheckUserExists(String email) {

        if (AllUsers.contains(email))
            return true;
        else return false;

    }

    public void Logout() {
        mAuth.signOut();
        Intent intent = new Intent(HomeActivity, MainActivity.class);
        HomeActivity.startActivity(intent);
    }

    public void setActivity(Activity HomeActiviy) {
        HomeActivity = (com.example.fitshare.HomeActivity) HomeActiviy;

    }

    public void deleteUserFromList(String email, String listID) {
        String userID = cleanUserName(email);
        myRef = database.getReference("Users");
        tempRef = database.getReference("AllLists");

        myRef.child(userID).child("lists").child(listID).removeValue();
        tempRef.child(listID).child("user").child(userID).removeValue();


    }

    public void CopyProduct(String listID, int listPosition) {
        DatabaseReference myRef;
        Bitmap imageBitmap;
        myRef = database.getReference("AllLists");
        tempRef = database.getReference("Users");
        int i = productsList.size();
        for (Products product : productsList) {
            for (Products pro : CopyproductsList) {
                if (pro.getName().equals(product.getName())) {
                    i--;
                    break;
                }

            }
            if (!product.getImgUrl().equals("noImage")) {

                ImageModel.copyImage(product.getImgUrl(), product.getName() + listID, new ImageModel.Listener() {
                    @Override
                    public void onSuccess(String url) {
                        product.setImgUrl(url);
                        product.setSelected(false);
                        product.setListID(listID);
                        ModelProductDao.instance.addProducts(product, new ModelProductDao.AddProductsListener() {
                            @Override
                            public void onComplete() {
                                myRef.child(listID).child("Products").child(product.getName()).setValue(product);
                            }
                        });
                        //    myRef.child(listID).child("Products").child(product.getName()).setValue(product);
                    }

                    @Override
                    public void onFail() {


                    }
                });

            } else {
                product.setSelected(false);
                product.setListID(listID);
                myRef.child(listID).child("Products").child(product.getName()).setValue(product);
            }

        }

        int finalI = i;
//        ModelListDao.instance.getAllMyLists(user.getId(), new ModelListDao.GetAllMyListsListener() {
//            @Override
//            public void onComplete(List<myLists> data) {
//                List<myLists> list = data;
//
//                int j = list.get(listPosition).getListCount() + finalI;
//                list.get(listPosition).setListCount(j);
//                ModelListDao.instance.addMyList(list.get(listPosition), new ModelListDao.AddMyListsListener() {
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//
//                for (String userID : listUser) {
//                    userID = cleanUserName(userID);
//                    tempRef.child(userID).child("lists").child(listID).child("listCount").setValue(list.get(listPosition).getListCount());
//
//                }
//                myRef.child(listID).child("listCount").setValue(list.get(listPosition).getListCount());
//            }
//        });

//
//            for (String userID : listUser) {
//                userID = cleanUserName(userID);
//                tempRef.child(userID).child("lists").child(listID).child("listCount").setValue(list[0].get(listPosition).getListCount());
//
//            }
//            myRef.child(listID).child("listCount").setValue(list[0].get(listPosition).getListCount());

    }

    public void changeLanguage(String language) {
        DatabaseReference myRef;

        myRef = database.getReference("Users");
        myRef.child(userID).child("language").setValue(language);
        user.setLanguage(language);
        ModelUserDao.instance.addUser(user, new ModelUserDao.AddUserListener() {
            @Override
            public void onComplete() {

            }
        });
    }
//     public void UpdateLocalData()
//        {
//            ModelListDao.instance.getAllMyLists(user.getId(), new ModelListDao.GetAllMyListsListener() {
//                @Override
//                public void onComplete(List<myLists> data) {
//
//                }
//            });
//
//        }

    public void RefreshMyLists() {


        myRef = database.getReference("Users");


        ModelUserDao.instance.getAllUser(userID, new ModelUserDao.GetAllUserListener() {
            @Override
            public void onComplete(List<User> data) {
                if (data.size() != 0)
                    user = data.get(0);
//                user.setEmail(user.getId());
//                user.setId(cleanUserName(user.getId()));
            }
        });


        tempRef2 = database.getReference("AllLists");

        myRef = myRef.child(userID).child("lists");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    myList = (myLists) postSnapshot.getValue(myLists.class);
                    ModelListDao.instance.addMyList(myList, new ModelListDao.AddMyListsListener() {
                        @Override
                        public void onComplete() {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + databaseError.getMessage());

            }

        });


    }
}
