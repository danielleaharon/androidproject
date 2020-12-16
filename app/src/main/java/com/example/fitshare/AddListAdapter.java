package com.example.fitshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitshare.model.ModelFirebase;
import com.example.fitshare.model.myLists;

import java.util.ArrayList;
import java.util.List;

public class AddListAdapter extends RecyclerView.Adapter<AddListAdapter.ViewHolder> {

    interface onItemClickListenr {
        void onClick(int position);
    }

    static HomeActivity parent;
    Activity activity;
    LayoutInflater inflater;
    static List<String> UserList = new ArrayList<>();
    private onItemClickListenr listener;


    public AddListAdapter(HomeActivity activity, List<String> UserList) {
        this.parent = activity;
        this.activity=activity;
        this.UserList = UserList;
        this.inflater = activity.getLayoutInflater();
    }

    void setonItemClickListenr(onItemClickListenr listener) {
        this.listener = (onItemClickListenr) listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.user_item, parent, false);


        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }



    @Override
    public int getItemCount() {
        return UserList.size();
    }



    public void updateList(List<String> UserList) {
        this.UserList = (UserList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView name;
        public Button delete_btn;

        int i;
        public onItemClickListenr listener;


        public ViewHolder(final View view, final onItemClickListenr listener) {
            super(view);
            mView = view;
            name = mView.findViewById(R.id.name);
            delete_btn=mView.findViewById(R.id.delete_btn);

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(parent.listID!=null)
                    {
                        ModelFirebase.instance.deleteUserFromList(UserList.get(getAdapterPosition()).trim(),parent.listID);
                    }

                    UserList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
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
            name.setText(UserList.get(position));
            if(ModelFirebase.instance.getUser().email.equals(UserList.get(position)))
                delete_btn.setVisibility(View.INVISIBLE);


        }
    }
}


