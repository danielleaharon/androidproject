package com.example.fitshare;

import android.annotation.SuppressLint;
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

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    interface onItemClickListenr {
        void onClick(int position);
    }

    @SuppressLint("StaticFieldLeak")
    static HomeActivity parent;
    private Activity activity;
    private LayoutInflater inflater;
    static List<myLists> myLists = new ArrayList<>();
    private onItemClickListenr listener;


    public MyListAdapter(Activity activity) {
        this.activity = activity;
        parent = (HomeActivity) activity;
        this.inflater = activity.getLayoutInflater();
    }

    void setonItemClickListenr(onItemClickListenr listener) {
        this.listener = (onItemClickListenr) listener;
    }

    public void setList(List<myLists> lists) {
        myLists = lists;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity)
                .inflate(R.layout.my_list_item, parent, false);


        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return myLists.size();
    }


    static public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView name;
        private TextView product_count;
        private onItemClickListenr Listener;


        public ViewHolder(final View view, final onItemClickListenr listener) {
            super(view);
            this.mView = view;
            this.product_count = mView.findViewById(R.id.product_count);
            this.name = mView.findViewById(R.id.name);
            this.Listener = listener;

            mView.setOnClickListener(v -> {
                if (Listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listener.onClick(position);
                    }
                }
            });


        }


        @SuppressLint("SetTextI18n")
        public void bind(int position) {


            name.setText(myLists.get(position).getListName());
            if (parent.user.getLanguage().equals("Hebrew")) {
                String string="מספר מוצרים: " + myLists.get(position).getListCount();
                product_count.setText(string);
            }
            else {
                String string="Number of products: " + myLists.get(position).getListCount();
                product_count.setText(string);
            }


        }
    }
}

