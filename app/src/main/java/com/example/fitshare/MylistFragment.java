package com.example.fitshare;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fitshare.model.User;
import com.example.fitshare.model.myLists;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    RecyclerView listView;
     HomeActivity parent;
    MyListAdapter MyListAdapter;
    List<myLists> myLists;
    int position;
    DatabaseReference myRef;
    DatabaseReference myRef2;
    public MylistFragment() {
        // Required empty public constructor
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
        myRef = FirebaseDatabase.getInstance().getReference("Users");
    //    String userID=MylistFragmentArgs.fromBundle(getArguments()).getUserID();
        //String userID="-MN3z0RJ1qxHEecTzJ3g";
        final String userID=parent.id.trim();
        Log.d("TAG", userID );

        myRef = myRef.child(userID);
   //      User value = new User();
        // Toast.makeText(HomeActivity.this, id, Toast.LENGTH_SHORT).show();
        Log.d("TAG", userID );
        final User[] value = {new User()};

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Getting the string value of that node


                value[0] = dataSnapshot.getValue(User.class);

                Toast.makeText(parent, "Data Received: "+ value[0].email, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: Something went wrong! Error:" + databaseError.getMessage());

            }

        });
         myLists = value[0].myLists;

        value[0].CreateList();

        listView= view.findViewById(R.id.my_list_recycler);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManger=new LinearLayoutManager(parent);
        listView.setLayoutManager(layoutManger);

          MyListAdapter = new MyListAdapter(parent,myLists);
        listView.setAdapter((RecyclerView.Adapter) MyListAdapter);
        MyListAdapter.setonItemClickListenr(new MyListAdapter.onItemClickListenr() {
            @Override
            public void onClick(int position) {


                Log.d("TAG","click"+position);



            }
        });
        Button add_item=view.findViewById(R.id.add_item);
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value[0].addtolist("new list"+position);
                myLists = value[0].myLists;
                myRef2 = FirebaseDatabase.getInstance().getReference("Lists");
                myRef2.child(userID).setValue(myLists);
                MyListAdapter.notifyDataSetChanged();
                MyListAdapter.updateList(myLists);
                listView.setAdapter((RecyclerView.Adapter) MyListAdapter);
            }
        });



        return view;
    }
}