package com.example.fitshare;


import android.app.Activity;

import android.os.Build;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.CompoundButton;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fitshare.model.ModelFirebase;
import com.example.fitshare.model.ModelProductDao;
import com.example.fitshare.model.Products;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    interface onItemClickListenr {
        void onClick(int position);
    }

    static HomeActivity parent;
    Activity activity;
    LayoutInflater inflater;
    static ProductsFragment productsFragment;
    static List<Products> Products_list = new ArrayList<>();

    ImageView image;

    private onItemClickListenr listener;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ProductsAdapter(HomeActivity activity) {
        this.activity = activity;
        parent = activity;
   //     this.productsFragment = productsFragment;
//        this.Products_list = Products_list;
        reloadData();

        this.inflater = activity.getLayoutInflater();
    }

    void setonItemClickListenr(onItemClickListenr listener) {
        this.listener = (onItemClickListenr) listener;
    }
    void reloadData(){

        ModelProductDao.instance.getAllProducts(parent.listID,new ModelProductDao.GetAllProductsListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(List<Products> data) {
                Products_list = data;
                SortProducts();
                notifyDataSetChanged();
            }

        });
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.product_item, parent, false);


        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return Products_list.size();
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SortProducts() {
        Products_list.sort(new Comparator<Products>() {
            @Override
            public int compare(com.example.fitshare.model.Products o1, com.example.fitshare.model.Products o2) {
                if (o2.isSelected()) return -1;
                return 1;
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView name;
        public TextView Amount_product;
        public android.widget.CheckBox CheckBox;
        public onItemClickListenr listener;
        int i;

        public ViewHolder(final View view, final onItemClickListenr listener) {
            super(view);
            mView = view;
            i = getAdapterPosition();
            this.listener = listener;
            Amount_product = view.findViewById(R.id.Amount_product);
            image = view.findViewById(R.id.image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.openImage(Products_list.get(i), parent.listID, Products_list.get(i).getImgUrl());
                }
            });

            name = (TextView) view.findViewById(R.id.name);
            CheckBox = view.findViewById(R.id.select);
            CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {

                        Products_list.get(i).setSelected(true);
                        ModelFirebase.instance.UpdateProducts(Products_list.get(i), parent.listID);
                        reloadData();

                    } else {


                        Products_list.get(i).setSelected(false);
                        ModelFirebase.instance.UpdateProducts(Products_list.get(i), parent.listID);
                        reloadData();


                    }

                    Log.d("TAG", "" + Products_list.get(i).isSelected() + "," + CheckBox.isChecked());


                }
            });


            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onClick(position);
                        }
                    }
                }
            });

        }


        public void bind(int position) {

            this.i = position;
            name.setText(Products_list.get(position).getName());
            CheckBox.setChecked(Products_list.get(position).isSelected());

            String[] amountArr = Products_list.get(position).getAmount().split(" ");
            String amount1 = amountArr[0];
            String amount2 = amountArr[1];
            if (parent.value.getLanguage().equals("Hebrew")) {
                if (amount2.equals("Units"))
                    amount2 = "יח׳";
                else if (amount2.equals("Gm"))
                    amount2 = "גרם";
                else amount2 = "קילוגרם";

            }

            String new_amount = amount1 + " " + amount2;
            Amount_product.setText(new_amount);
            if (!Products_list.get(position).getImgUrl().equals("noImage")) {
                Picasso.get().load(Products_list.get(position).getImgUrl()).into(image);

            }


        }
    }

}
