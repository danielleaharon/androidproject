package com.example.fitshare;

import android.content.Context;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MylistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    User user;
    String userID;
    RecyclerView listView;
    HomeActivity parent;
    MyListAdapter MyListAdapter;
//    ListViewModel listViewModel;
//    LiveData<List<myLists>> liveData;
//    List<myLists> Data= new ArrayList<>();

    public MylistFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MylistFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        this.user = parent.value;

        //    listViewModel=new ViewModelProvider(this).get(ListViewModel.class);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mylist, container, false);
        parent.setTitle("רשימות");
        parent.CorrectList = null;
        parent.addList_btn.setBackgroundResource(R.drawable.addlisticon);
        listView = view.findViewById(R.id.my_list_recycler);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManger = new LinearLayoutManager(parent);
        listView.setLayoutManager(layoutManger);
        new ItemTouchHelper(ItemTouchHelperCallback).attachToRecyclerView(listView);

        MyListAdapter = new MyListAdapter(parent, user.getMyLists());

        listView.setAdapter((RecyclerView.Adapter) MyListAdapter);
        MyListAdapter.setonItemClickListenr(new MyListAdapter.onItemClickListenr() {
            @Override
            public void onClick(int position) {

                String listid = user.getMyLists().get(position).listID.trim();
                parent.openList(listid, user.getMyLists().get(position).ListName, position);
                Log.d("TAG", "open " + listid);


            }
        });

//        liveData = listViewModel.getData();
//        liveData.observe(getViewLifecycleOwner(), new Observer<List<myLists>>() {
//            @Override
//            public void onChanged(List<myLists> myLists) {
//                Data=myLists;
//                MyListAdapter.notifyDataSetChanged();
//            }
//        });
//         newListName_edit = view.findViewById(R.id.newListName_edit);
//        Button add_item=view.findViewById(R.id.add_list);
//        add_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                String listName=newListName_edit.getText().toString().trim();
//
//                if(TextUtils.isEmpty(listName))
//                {
//                    newListName_edit.setError("need name");
//                    return;
//                }
//                for (com.example.fitshare.model.myLists name: user.getMyLists())
//                {
//                    if(name.ListName.equals(listName)){
//                        newListName_edit.setError("The name is busy");
//                    return;}
//                }
//
//
//
////                ModelFirebase.instance.newList(listName,parent.userID, user);
//
//
//                MyListAdapter.updateList((List<com.example.fitshare.model.myLists>) user.getMyLists());
//                listView.setAdapter((RecyclerView.Adapter) MyListAdapter);
//
//
//                InputMethodManager inputManager = (InputMethodManager)
//                        parent.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                inputManager.hideSoftInputFromWindow(parent.getCurrentFocus().getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        });


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
            MyListAdapter.updateList(user.getMyLists());
            listView.setAdapter((RecyclerView.Adapter) MyListAdapter);
        }
    };
}