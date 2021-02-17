package com.example.fitshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitshare.model.ModelList;
import java.util.ArrayList;
import java.util.List;

public class AddListAdapter extends RecyclerView.Adapter<AddListAdapter.ViewHolder> {


    static HomeActivity parent;
    Activity activity;
    LayoutInflater inflater;
    static List<String> UserList = new ArrayList<>();


    public AddListAdapter(HomeActivity activity, List<String> UserList) {
        parent = activity;
        this.activity = activity;
        AddListAdapter.UserList = UserList;
        this.inflater = activity.getLayoutInflater();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.user_item, parent, false);

        return new ViewHolder(view);
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
        AddListAdapter.UserList = (UserList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView name;
        public Button delete_btn;
        int i;

        public ViewHolder(final View view) {
            super(view);
            mView = view;
            name = mView.findViewById(R.id.name);
            delete_btn = mView.findViewById(R.id.delete_btn);

            delete_btn.setOnClickListener(v -> {
                if (parent.listID != null) {
                    ModelList.instance.DeleteUserToList(UserList.get(getAdapterPosition()).trim(), parent.CorrectList);
                }

                UserList.remove(getAdapterPosition());
                notifyDataSetChanged();
            });

        }


        public void bind(int position) {

            this.i = position;
            name.setText(UserList.get(position));

            if (parent.user.getEmail().equals(UserList.get(position)))
                delete_btn.setVisibility(View.INVISIBLE);


        }
    }
}


