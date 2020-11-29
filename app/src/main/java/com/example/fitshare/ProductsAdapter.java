package com.example.fitshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitshare.model.myLists;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    interface onItemClickListenr{
        void onClick(int position);
    }
    HomeActivity parent;
    Activity activity;
    LayoutInflater inflater;
    static List<com.example.fitshare.model.myLists> myLists =new ArrayList<>();


    private MyListAdapter.onItemClickListenr listener;

    public ProductsAdapter(Activity activity,List<myLists> mylist)
    {
        this.activity=activity;
        myLists=mylist;
        this.inflater=activity.getLayoutInflater();
    }
    void setonItemClickListenr(onItemClickListenr listener){
        this.listener= (MyListAdapter.onItemClickListenr) listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.my_list_item, parent, false);


        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return myLists.size();
    }
    public void updateList(List<myLists> myLists)
    {
        this.myLists=myLists;
    }
    static public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView name;

        int i;
        public MyListAdapter.onItemClickListenr listener;


        public ViewHolder(final View view, final MyListAdapter.onItemClickListenr listener) {
            super(view);
            mView = view;
            name = mView.findViewById(R.id.name);
            this.listener = listener;
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
            name.setText(myLists.get(position).ListName);


        }
    }
}
