package com.example.fitshare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.fitshare.model.ModelFirebase;
import com.example.fitshare.model.User;
import com.example.fitshare.model.myLists;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class MylistFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    User user;
    RecyclerView listView;
    HomeActivity parent;
   private MyListAdapter MyListAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;


    public MylistFragment() {
    }

    public static MylistFragment newInstance(String param1, String param2) {
        MylistFragment fragment = new MylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent = (HomeActivity) getActivity();
        this.user = ModelFirebase.instance.getUser();
        ModelFirebase.instance.setActivity(parent);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mylist, container, false);
       if( parent.value.language.equals("Hebrew"))
           parent.setTitle("רשימות");
       else parent.setTitle("Lists");
        parent.CorrectList = null;
        parent.listID=null;
        parent.save_btn.setVisibility(View.INVISIBLE);
        parent.logout_btn.setVisibility(View.VISIBLE);
        parent.addList_btn.setVisibility(View.INVISIBLE);
        parent.copy_list_btn.setVisibility(View.INVISIBLE);
        parent.language_btn.setVisibility(View.VISIBLE);

        parent.floating_addList_btn.show();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);


        listView = view.findViewById(R.id.my_list_recycler);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManger = new LinearLayoutManager(parent);
        listView.setLayoutManager(layoutManger);
        new ItemTouchHelper(ItemTouchHelperCallback).attachToRecyclerView(listView);

        MyListAdapter = new MyListAdapter(parent);

        listView.setAdapter((RecyclerView.Adapter) MyListAdapter);
        MyListAdapter.setonItemClickListenr(new MyListAdapter.onItemClickListenr() {
            @Override
            public void onClick(int position) {

                String listid = user.getMyLists().get(position).listID.trim();
                parent.openList(listid, user.getMyLists().get(position).ListName, position);
                Log.d("TAG", "open " + listid);


            }
        });




        return view;
    }

    //Placing the possibility of skidding in the circular list
    ItemTouchHelper.SimpleCallback ItemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            parent.db.removeList(user.myLists.get(viewHolder.getAdapterPosition()));
            user.myLists.remove(viewHolder.getAdapterPosition());
            MyListAdapter.notifyDataSetChanged();
            MyListAdapter.updateList();
            listView.setAdapter((RecyclerView.Adapter) MyListAdapter);
        }

    };

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
    }
    public void refreshList(){

        ModelFirebase.instance.refreshUserList();
        MyListAdapter.notifyDataSetChanged();
        MyListAdapter.updateList();
        listView.setAdapter((RecyclerView.Adapter) MyListAdapter);
        swipeRefreshLayout.setRefreshing(false);

    }
}