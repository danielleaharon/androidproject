package com.example.fitshare;


import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.example.fitshare.model.ModelList;
import com.example.fitshare.model.ModelUser;
import com.example.fitshare.model.User;
import com.example.fitshare.model.myLists;

import java.util.ArrayList;
import java.util.List;


public class MylistFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    User user;
    List<myLists> myListsData = new ArrayList<>();
    myListsViewModel viewModel;
    LiveData<List<myLists>> LiveData;
    RecyclerView listView;
    HomeActivity parent;
    private MyListAdapter MyListAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;


    public MylistFragment() {
    }

    public static MylistFragment newInstance(String param1, String param2) {
        MylistFragment fragment = new MylistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(myListsViewModel.class);

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mylist, container, false);
        this.user = ModelUser.instance.getUser();

        if (user.getLanguage().equals("Hebrew"))
            parent.setTitle("רשימות");
        else parent.setTitle("Lists");

        parent.CorrectList = null;
        parent.listID = null;
        parent.ListName = null;
        parent.save_btn.setVisibility(View.INVISIBLE);
        parent.logout_btn.setVisibility(View.VISIBLE);
        parent.addList_btn.setVisibility(View.INVISIBLE);
        parent.copy_list_btn.setVisibility(View.INVISIBLE);
        parent.language_btn.setVisibility(View.VISIBLE);
        parent.colorbtn.setVisibility(View.VISIBLE);
        parent.floating_addList_btn.show();

        view.setBackgroundColor(parent.color);

        viewModel.getData(new myListsViewModel.GetAllMyListsListener() {
            @Override
            public void onComplete(androidx.lifecycle.LiveData<List<myLists>> data) {
                LiveData=data;

                LiveData.observe(getViewLifecycleOwner(), new Observer<List<com.example.fitshare.model.myLists>>() {
                    @Override
                    public void onChanged(List<com.example.fitshare.model.myLists> myLists) {
                        myListsData = myLists;
                        parent.AllMyLists=myLists;
                        MyListAdapter.setList(myListsData);

                    }
                });
            }
        });


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

                String listid = myListsData.get(position).getListID().trim();
                parent.openList(listid, myListsData.get(position).getListName(), position);
                Log.d("TAG", "open, position:" + position + ", List name: " + myListsData.get(position).getListName());


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

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


            Button yes_btn;
            Button cancel_btn;
            TextView delete_txt;

            Dialog dialog1 = new Dialog(parent);
            dialog1.setContentView(R.layout.delete_dialog);
            dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Window window = dialog1.getWindow();
            window.setGravity(Gravity.CENTER);

            cancel_btn = dialog1.findViewById(R.id.cancel_btn);
            yes_btn = dialog1.findViewById(R.id.yes_btn);
            delete_txt = dialog1.findViewById(R.id.delete_txt);


            if (user.getLanguage().equals("English")) {
                yes_btn.setText("yes");
                delete_txt.setText("Are you sure you want to delete?");
            }

            yes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                  ModelList.instance.deleteMyList(myListsData.get(viewHolder.getAdapterPosition()), new ModelList.deleteMyListsListener() {
                      @Override
                      public void onComplete() {

                          dialog1.cancel();
                      }
                  });

                }
            });
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyListAdapter.notifyDataSetChanged();

                    listView.setAdapter((RecyclerView.Adapter) MyListAdapter);
                    dialog1.cancel();
                }
            });


            dialog1.setCancelable(false);
            window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog1.show();


        }

    };

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);
        viewModel.Refresh(new myListsViewModel.RefreshMyListsListener() {
            @Override
            public void onComplete() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });    }


}