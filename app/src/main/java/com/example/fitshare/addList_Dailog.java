package com.example.fitshare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitshare.model.ModelFirebase;
import com.example.fitshare.model.myLists;

import java.util.ArrayList;
import java.util.List;

public class addList_Dailog extends AppCompatDialogFragment  {
    HomeActivity parent;
    EditText listName_edit;
    RecyclerView listView;
    AddListAdapter addListAdapter;
    List<String > user= new ArrayList<>();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_list,null);
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
                ModelFirebase.instance.newList(listName,parent.userID, user);

                dismiss();


            }
        });
        Button cancel_btn=view.findViewById(R.id.cancel);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
     builder.setView(view).setTitle("");

      Button add_friend_btn=view.findViewById(R.id.add_friend_btn);
       add_friend_btn.setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View v) {
            addUser_Dialog dialog = new addUser_Dialog();
            dialog.show(parent.getSupportFragmentManager(),"add user");
           String data= dialog.getData();



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

        return builder.create();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent = (HomeActivity) getActivity();

    }


}
