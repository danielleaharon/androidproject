package com.example.fitshare;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitshare.model.ModelList;
import com.example.fitshare.model.ModelProduct;
import com.example.fitshare.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {


    static HomeActivity parent;
    private Activity activity;
    private LayoutInflater inflater;
    public List<Products> Products_list = new ArrayList<>();
    private onItemClickListenr listener;


    public void setList(List<Products> products_list) {
        Products_list.clear();
        this.Products_list = products_list;
        notifyDataSetChanged();
    }

    interface onItemClickListenr {
        void onClick(int position);
    }

    public ProductsAdapter(HomeActivity activity) {
        this.activity = activity;
        parent = activity;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView name;
        private ImageView image;
        private TextView Amount_product;
        private android.widget.CheckBox CheckBox;
        private onItemClickListenr listener;
        private int i;

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
            CheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Products_list.get(i).isSelected()) {

                        Products_list.get(i).setSelected(false);

                    } else {

                        Products_list.get(i).setSelected(true);
                    }

                    ModelProduct.instance.updateProducts(Products_list.get(i), new ModelList.updateMyListsListener() {
                        @Override
                        public void onComplete() {
                            Log.d("TAG", "" + Products_list.get(i).isSelected() + "," + CheckBox.isChecked());
                        }
                    });

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

            this.i = getAdapterPosition();

            if (Products_list.get(i).getImgUrl().contains("noImage")) {
                Picasso.get().cancelRequest(image);
                image.setImageDrawable(null);
                image.setImageResource(R.drawable.nophoto);

            } else {
                image.setImageDrawable(null);
                Picasso.get().load(Products_list.get(position).getImgUrl()).placeholder(R.drawable.nophoto).error(R.drawable.nophoto).into(image);
            }
            name.setText(Products_list.get(i).getName());
            CheckBox.setChecked(Products_list.get(i).isSelected());

            String[] amountArr = Products_list.get(i).getAmount().split(" ");
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


        }
    }

}
