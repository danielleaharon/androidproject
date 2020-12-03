package com.example.fitshare;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    //List<myLists> myUserLists;
    int position;
    EditText newListName_edit;
    DatabaseReference myRef;
    DatabaseReference myRef2;
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
        parent= (HomeActivity) getActivity();
        this.user =parent.value;
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

        listView= view.findViewById(R.id.my_list_recycler);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManger=new LinearLayoutManager(parent);
        listView.setLayoutManager(layoutManger);

        MyListAdapter = new MyListAdapter(parent, user.getMyLists());

        listView.setAdapter((RecyclerView.Adapter) MyListAdapter);
        MyListAdapter.setonItemClickListenr(new MyListAdapter.onItemClickListenr() {
            @Override
            public void onClick(int position) {

                String listName = user.getMyLists().get(position).ListName.trim();
               parent.openList(listName,position);
                Log.d("TAG","open "+listName);



            }
        });
         newListName_edit = view.findViewById(R.id.newListName_edit);
        Button add_item=view.findViewById(R.id.add_list);
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String listName=newListName_edit.getText().toString().trim();

                if(TextUtils.isEmpty(listName))
                {
                    newListName_edit.setError("need name");
                    return;
                }
                for (com.example.fitshare.model.myLists name: user.getMyLists())
                {
                    if(name.ListName.equals(listName)){
                        newListName_edit.setError("The name is busy");
                    return;}
                }


                myLists myNewList =new myLists(listName);
               user.addtolist(myNewList);

                ModelFirebase.instance.newList(listName,parent.userID);


                MyListAdapter.updateList((List<com.example.fitshare.model.myLists>) user.getMyLists());
                listView.setAdapter((RecyclerView.Adapter) MyListAdapter);


                InputMethodManager inputManager = (InputMethodManager)
                        parent.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(parent.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });



        return view;
    }
}