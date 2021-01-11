package com.example.fitshare.model;


import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ModelFirebaseProducts {
    public static final ModelFirebaseProducts instance = new ModelFirebaseProducts();

    FirebaseDatabase database;
    List<Products> productsList;
    List<Products> CopyproductsList;
    List<String> listUser;


    private ModelFirebaseProducts() {
        database = FirebaseDatabase.getInstance();


    }
    public interface ProductsListener {
        void onComplete() ;
    }
    public List<Products> getProductsList() {
        return productsList;
    }

    public List<Products> getCopyproductsList() {
        return CopyproductsList;
    }

    public void newProducts(Products Products, String ListID,ProductsListener listener) {
        DatabaseReference myRef;
        DatabaseReference tempRef;
        myRef = database.getReference("AllLists");
        tempRef = database.getReference("Users");
        productsList.add(Products);
        ModelFirebaseMyList.instance.getUserOfList(ListID, new ModelFirebaseMyList.UserListListener() {
            @Override
            public void onComplete(List<String> data) {
                listUser= data;
                for (String userID : listUser) {
                    userID = ModelFirebaseUser.instance.cleanUserName(userID);
                    tempRef.child(userID).child("lists").child(ListID).child("listCount").setValue(productsList.size());
                    myRef.child(ListID).child("listCount").setValue(productsList.size());
                    myRef.child(ListID).child("Products").child(Products.getName()).setValue(Products);
                    listener.onComplete();
                }
            }
        });





    }

    public void UpdateProducts(Products Products, String ListID, ProductsListener listener) {
        DatabaseReference myRef;
        myRef = database.getReference("AllLists");

        myRef.child(ListID).child("Products").child(Products.getName()).setValue(Products);
        listener.onComplete();
    }
    public interface GetProductsListener {
        void onComplete(List<Products> data, List<String> userList);
    }

    public void getListProducts(String id, GetProductsListener listener) {

        DatabaseReference myRef;
        DatabaseReference tempRef;
        this.productsList = new ArrayList<>();
        String listID = id.trim();
        listUser = new ArrayList<>();
        myRef = database.getReference("AllLists");
        tempRef = myRef.child(listID).child("user");
        myRef = myRef.child(listID).child("Products");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Products products = postSnapshot.getValue(Products.class);

                    productsList.add(products);

                }

                tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            String user = postSnapshot.getValue(String.class);
                            if(!listUser.contains(user))
                            listUser.add(user);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                listener.onComplete(productsList, listUser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + error.getMessage());
            }
        });



    }

    public void removeProducts(Products product,int position, String id ,ProductsListener listener) {
        DatabaseReference myRef;
        DatabaseReference tempRef;
        myRef = database.getReference("AllLists");
        tempRef = database.getReference("Users");

        productsList.remove(position);

        for (String user : listUser) {
            String userID = ModelUser.instance.cleanUserName(user);
            tempRef.child(userID).child("lists").child(id).child("listCount").setValue(productsList.size());
        }

        myRef.child(id).child("listCount").setValue(productsList.size());
        myRef.child(id).child("Products").child(product.getName()).child("delete").setValue(true);
        if (!product.getImgUrl().equals("noImage"))
            ImageModel.deleteImage(product.getImgUrl());

        listener.onComplete();

    }

    public interface CopyProductsListener {
        void onComplete();
    }
        public void getCopyListData(String id, int listPosition,CopyProductsListener listener) {


        DatabaseReference myRef;
        DatabaseReference tempRef;
        CopyproductsList = new ArrayList<>();
        String listID = id.trim();

            myRef = database.getReference("AllLists");

            myRef = myRef.child(listID).child("Products");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Products  pro = postSnapshot.getValue(Products.class);
                    CopyproductsList.add(pro);


                }

                CopyProduct(listID, listPosition, new CopyProductsListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + error.getMessage());
            }
        });

    }
    public void CopyProduct(String listID, int listPosition,CopyProductsListener listener) {
        DatabaseReference myRef;
        DatabaseReference tempRef;
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
                            myRef.child(listID).child("Products").child(product.getName()).setValue(product);

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
        List<Products>  list= getCopyproductsList();
        int j = list.size() + finalI;

                ModelFirebaseMyList.instance.getUserOfList(listID, new ModelFirebaseMyList.UserListListener() {
                    @Override
                    public void onComplete(List<String> data) {
                       List<String> listUser =data;
                        for (String userID : listUser) {
                            userID = ModelUser.instance.cleanUserName(userID);
                            tempRef.child(userID).child("lists").child(listID).child("listCount").setValue(j);

                        }
                        myRef.child(listID).child("listCount").setValue(j);

                        listener.onComplete();
                    }

                });
            }




}
