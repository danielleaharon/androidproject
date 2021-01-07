package com.example.fitshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitshare.model.ModelListDao;
import com.example.fitshare.model.myLists;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    interface onItemClickListenr{
        void onClick(int position);
    }
   static HomeActivity parent;
    Activity activity;
    LayoutInflater inflater;
    static List<myLists> myLists =new ArrayList<>();


    private onItemClickListenr listener;


    public MyListAdapter(Activity activity)
    {
        this.activity=activity;
        this.parent= (HomeActivity) activity;
   //     reloadData();
       // myLists=ModelFirebase.instance.getUser().getMyLists();
//        ModelDao.instance.getAllMyLists(new ModelDao.GetAllMyListsListener() {
//            @Override
//            public void onComplete(List<myLists> data) {
//                myLists=data;
//            }
//        });
//        reloadData();

        this.inflater=activity.getLayoutInflater();
    }


    void reloadData(){

        ModelListDao.instance.getAllMyLists(parent.value.getId(),new ModelListDao.GetAllMyListsListener() {
            @Override
            public void onComplete(List<myLists> data) {
                myLists = data;
                notifyDataSetChanged();
            }
        });
    }
    void setonItemClickListenr(onItemClickListenr listener){
        this.listener= (onItemClickListenr) listener;
    }



    public  void setList(List<myLists> lists)
    {
        this.myLists=lists;
        notifyDataSetChanged();

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


    static public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView name;
        public TextView product_count;

        int i;
        public onItemClickListenr listener;


        public ViewHolder(final View view, final onItemClickListenr listener) {
            super(view);
            mView = view;
            product_count=mView.findViewById(R.id.product_count);
           name=mView.findViewById(R.id.name);
            this.listener=listener;
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION)
                        {
                            listener.onClick(position);
                        }
                    }
                }
            });


        }



        public void bind( int position) {

            this.i= position;
            name.setText(myLists.get(position).getListName());
            if(parent.value.getLanguage().equals("Hebrew"))
            product_count.setText("מספר מוצרים: " +myLists.get(position).getListCount());
            else {
                product_count.setText("Number of products: " + myLists.get(position).getListCount());
            }







        }
    }
}

