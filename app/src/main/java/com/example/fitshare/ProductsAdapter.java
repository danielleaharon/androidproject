package com.example.fitshare;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitshare.model.ImageModel;
import com.example.fitshare.model.ModelFirebase;
import com.example.fitshare.model.Products;
import com.example.fitshare.model.myLists;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    boolean onBind = false;
    ImageView image;

    private onItemClickListenr listener;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ProductsAdapter(HomeActivity activity, List<Products> Products_list, ProductsFragment productsFragment) {
        this.activity = activity;
        parent = activity;
        this.productsFragment = productsFragment;
        this.Products_list = Products_list;
        SortProducts();
        this.inflater = activity.getLayoutInflater();
    }

    void setonItemClickListenr(onItemClickListenr listener) {
        this.listener = (onItemClickListenr) listener;
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
    public void updateList() {


        this.Products_list = ModelFirebase.instance.getProductsList();
        SortProducts();
        notifyDataSetChanged();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SortProducts() {
        Products_list.sort(new Comparator<Products>() {
            @Override
            public int compare(com.example.fitshare.model.Products o1, com.example.fitshare.model.Products o2) {
                if (o2.selected) return -1;
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
                    parent.openImage(Products_list.get(i), parent.listID, Products_list.get(i).imgUrl);
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
                        parent.db.UpdateProducts(Products_list.get(i), parent.listID);

                    } else {


                        Products_list.get(i).setSelected(false);
                        parent.db.UpdateProducts(Products_list.get(i), parent.listID);


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
            name.setText(Products_list.get(position).name);
            CheckBox.setChecked(Products_list.get(position).isSelected());

            String[] amountArr = Products_list.get(position).Amount.split(" ");
            String amount1 = amountArr[0];
            String amount2 = amountArr[1];
            if (parent.value.language.equals("Hebrew")) {
                if(amount2.equals("Units"))
                 amount2 = "יח׳";
                else if(amount2.equals("Gm"))
                    amount2 = "גרם";
                else amount2 = "קילוגרם";

            }

            String new_amount = amount1 + " " + amount2;
            Amount_product.setText(new_amount);
            if (!Products_list.get(position).imgUrl.equals("noImage")) {
                Picasso.get().load(Products_list.get(position).imgUrl).into(image);

            }


        }
    }

}
