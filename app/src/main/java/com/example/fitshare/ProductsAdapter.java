package com.example.fitshare;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitshare.model.Products;
import com.example.fitshare.model.myLists;

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
    public void updateList(List<Products> Products_list) {


        this.Products_list = Products_list;
        SortProducts();


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
        public android.widget.CheckBox CheckBox;
        public onItemClickListenr listener;
        int i;

        public ViewHolder(final View view, final onItemClickListenr listener) {
            super(view);
            mView = view;
            i = getAdapterPosition();
            this.listener = listener;
            name = (TextView) view.findViewById(R.id.name);
            CheckBox = view.findViewById(R.id.select);
            CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {

                        Products_list.get(i).setSelected(true);
                        parent.db.UpdateProducts(Products_list.get(i), true, parent.listID);


//                if(!SelectedProprtieslist.contains( Proprtieslist.get(i)))
//                    SelectedProprtieslist.add(  Proprtieslist.get(i));
                    } else {
//                if( SelectedProprtieslist.contains( Proprtieslist.get(i)))
//                    SelectedProprtieslist.remove(Proprtieslist.get(i));

                        Products_list.get(i).setSelected(false);
                        parent.db.UpdateProducts(Products_list.get(i), false, parent.listID);


                    }

                    //   productsFragment.updateList(Products_list, i);
                    //      notifyDataSetChanged();


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

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void updateCheckedlist() {

            if (CheckBox.isChecked()) {
                Products_list.get(i).setSelected(true);
                parent.db.UpdateProducts(Products_list.get(i), true, parent.listID);
                Products products = Products_list.get(i);
                Products_list.remove(i);
                Products_list.add(products);

//                if(!SelectedProprtieslist.contains( Proprtieslist.get(i)))
//                    SelectedProprtieslist.add(  Proprtieslist.get(i));
            } else {
//                if( SelectedProprtieslist.contains( Proprtieslist.get(i)))
//                    SelectedProprtieslist.remove(Proprtieslist.get(i));

                Products_list.get(i).setSelected(false);
                parent.db.UpdateProducts(Products_list.get(i), false, parent.listID);

                Products products = Products_list.get(i);
                Products_list.remove(i);
                Products_list.add(products);


            }

            Log.d("TAG", "" + Products_list.get(i).isSelected() + "," + CheckBox.isChecked());


        }

        public void bind(int position) {

            this.i = position;
            name.setText(Products_list.get(position).name);
            CheckBox.setChecked(Products_list.get(position).isSelected());


        }
    }
}
