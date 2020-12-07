package com.example.fitshare;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.fitshare.model.myLists;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView listView;
    HomeActivity parent;
    AddListAdapter addListAdapter;
    EditText listName_edit;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<String > user= new ArrayList<>();

    public AddListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddListFragment newInstance(String param1, String param2) {
        AddListFragment fragment = new AddListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent= (HomeActivity) getActivity();
      //  this.user =parent.value;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list, container, false);
        listName_edit= view.findViewById(R.id.listName_edit);
        Button addList=view.findViewById(R.id.addList);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String listName=listName_edit.getText().toString().trim();

                if(TextUtils.isEmpty(listName))
                {
                    listName_edit.setError("need name");
                    return;


                }
                for (myLists name: parent.value.getMyLists())
                {
                    if(name.ListName.equals(listName)){
                        listName_edit.setError("The name is busy");
                        return;
                    }
                }
                if(parent.CorrectList!=null)
                {
                    parent.CorrectList.ListName=listName;
                    ModelFirebase.instance.UpdateListData(parent.CorrectList,parent.ListPosition);

                }else {
                    ModelFirebase.instance.newList(listName, parent.userID, user);
                }

                parent.navCtrl.popBackStack();


            }
        });
        Button cancel_btn=view.findViewById(R.id.cancel);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.navCtrl.popBackStack();
            }
        });


        Button add_friend_btn=view.findViewById(R.id.add_friend_btn);
        add_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              parent.openDialog();



            }
        });
        listView= view.findViewById(R.id.friend_ryc);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManger=new LinearLayoutManager(parent);
        listView.setLayoutManager(layoutManger);
        EditText List_name = view.findViewById(R.id.listName_edit);
        if(parent.CorrectList!=null) {
            List_name.setText(parent.CorrectList.ListName);
            addListAdapter = new AddListAdapter(parent, parent.db.getUserList());
        }
        else
        {
            user= parent.userDailog;

            addListAdapter = new AddListAdapter(parent, user);
        }

        listView.setAdapter((RecyclerView.Adapter) addListAdapter);
        addListAdapter.setonItemClickListenr(new AddListAdapter.onItemClickListenr() {
            @Override
            public void onClick(int position) {


            }
        });

        return view;
    }
}