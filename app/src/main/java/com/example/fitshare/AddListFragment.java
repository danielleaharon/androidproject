package com.example.fitshare;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.fitshare.model.ModelList;
import com.example.fitshare.model.ModelUser;
import com.example.fitshare.model.myLists;

import java.util.ArrayList;
import java.util.List;


public class AddListFragment extends Fragment {


    RecyclerView listView;
    HomeActivity parent;
    AddListAdapter addListAdapter;
    EditText listName_edit;
    List<String> ListOfUser = new ArrayList<>();

    //Dialog parameter
    String userName;


    public AddListFragment() {}



    public static AddListFragment newInstance(String param1, String param2) {
        AddListFragment fragment = new AddListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent = (HomeActivity) getActivity();

    }

    @Override
    public void onStop() {
        super.onStop();
        addListAdapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list, container, false);
        parent.save_btn.setVisibility(View.VISIBLE);
        parent.logout_btn.setVisibility(View.INVISIBLE);
        parent.addList_btn.setVisibility(View.INVISIBLE);
        parent.copy_list_btn.setVisibility(View.INVISIBLE);
        parent.language_btn.setVisibility(View.INVISIBLE);
        parent.floating_addList_btn.hide();
        parent.setTitle("");

        Button add_friend_btn = view.findViewById(R.id.add_friend_btn);
        TextView listName_txt = view.findViewById(R.id.listName_txt);
        TextView listFriend_txt = view.findViewById(R.id.listFriend_txt);
        listName_edit = view.findViewById(R.id.listName_edit);

        if (parent.value.getLanguage().equals("Hebrew")) {
            listName_txt.setText(R.string.listNameHebrew);
            listFriend_txt.setText(R.string.listFriendHebrew);
            add_friend_btn.setText("הוסף חבר חדש");

        } else {
            listName_txt.setText(R.string.listNameEnglish);
            listFriend_txt.setText(R.string.listFriendEnglish);
            add_friend_btn.setText(R.string.addFriendEnglish);
        }

        parent.save_btn.setOnClickListener(v -> {

            String listName = listName_edit.getText().toString().trim();
            if (parent.value.getLanguage().equals("Hebrew")) {
                if (TextUtils.isEmpty(listName)) {
                    listName_edit.setError("צריך שם לרשימה");
                    return;
                }
                for (myLists name : parent.AllMyLists) {
                    if (name.getListName().equals(listName)) {
                        if (parent.CorrectList != null)
                            if (parent.CorrectList.getListName().equals(listName))
                                break;
                        listName_edit.setError("השם תפוס");
                        return;
                    }
                }
            } else {
                if (TextUtils.isEmpty(listName)) {
                    listName_edit.setError("Need a name for the list");
                    return;

                }
                for (myLists name : parent.AllMyLists) {
                    if (name.getListName().equals(listName)) {
                        if (parent.CorrectList != null)
                            if (parent.CorrectList.getListName().equals(listName))
                                break;
                        listName_edit.setError("The name is busy");
                        return;
                    }
                }

            }
            if (parent.CorrectList != null) {
                if (!parent.CorrectList.getListName().equals(listName)) {
                    parent.CorrectList.setListName(listName);
                    parent.ListName = listName;
                }
                ModelList.instance.updateMyList(parent.CorrectList, null);
                for (String email : ListOfUser) {
                    ModelList.instance.addUserToList(email,parent.CorrectList);

                }

            } else {

                ModelList.instance.addMyList(listName, ListOfUser, null);
            }
            hideKeyboard(v);
            parent.navCtrl.popBackStack();


        });

        listName_edit.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        hideKeyboard(v);

                        return true;
                    default:
                        break;
                }
            }
            return false;
        });


        add_friend_btn.setOnClickListener(v -> {

            EditText user_edit;

            Dialog dialog1 = new Dialog(parent);

            dialog1.setContentView(R.layout.add_user);
            dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Window window = dialog1.getWindow();
            window.setGravity(Gravity.CENTER);

            user_edit = dialog1.findViewById(R.id.user_name_edit);
            Button cancel_btn = dialog1.findViewById(R.id.cancel_btn);

            cancel_btn.setOnClickListener(v1 -> dialog1.cancel());
            Button ok_btn = dialog1.findViewById(R.id.ok_btn);
            ok_btn.setOnClickListener(v12 -> {
                userName = user_edit.getText().toString().trim();
                if (parent.value.getLanguage().equals("Hebrew")) {
                    if (TextUtils.isEmpty(userName)) {
                        user_edit.setError("צריך שם משתמש");
                        return;
                    }
                    if (parent.value.getEmail().equals(userName)) {
                        user_edit.setError("אתה לא יכול להוסיף את עצמך!");
                        return;
                    }

                    ModelUser.instance.CheckUserExists(userName, new ModelUser.CheckUserExistsListener() {
                        @Override
                        public void onComplete(Boolean bool) {
                            if (bool == true) {
                                if (ListOfUser.contains(userName)) {
                                    user_edit.setError("משתמש כבר קיים ברשימה");
                                    return;
                                }
                                parent.addToUserDailog(userName);

                                if (parent.CorrectList != null) {
                                    addListAdapter.notifyDataSetChanged();
                                    addListAdapter.updateList(ListOfUser);
                                   // listView.setAdapter(addListAdapter);
                                }
                                dialog1.cancel();
                            } else {
                                user_edit.setError("משתמש לא קיים");
                                return;
                            }

                        }
                    });

                } else {
                    if (TextUtils.isEmpty(userName)) {
                        user_edit.setError("Need a username");
                        return;
                    }
                    if (parent.value.getEmail().equals(userName)) {
                        user_edit.setError("You can not add yourself!");
                        return;
                    }

                    ModelUser.instance.CheckUserExists(userName, new ModelUser.CheckUserExistsListener() {
                        @Override
                        public void onComplete(Boolean bool) {
                            if (bool == true) {
                                if (ListOfUser.contains(userName)) {
                                    user_edit.setError("A user already exists in the list");
                                    return;
                                }
                                parent.addToUserDailog(userName);

                                if (parent.CorrectList != null) {

                                    addListAdapter.notifyDataSetChanged();
                                    addListAdapter.updateList(ListOfUser);
                                   // listView.setAdapter(addListAdapter);
                                }
                                dialog1.cancel();
                            } else {
                                user_edit.setError("User does not exist");

                            }
                        }
                    });

                }

            });

            dialog1.setCancelable(true);
            window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog1.show();

        });
        listView = view.findViewById(R.id.friend_ryc);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManger = new LinearLayoutManager(parent);
        listView.setLayoutManager(layoutManger);
        EditText List_name = view.findViewById(R.id.listName_edit);
        if (parent.CorrectList != null) {
            List_name.setText(parent.CorrectList.getListName());
        }
        ListOfUser = parent.userDailog;

        addListAdapter = new AddListAdapter(parent, ListOfUser);

        listView.setAdapter(addListAdapter);


        return view;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}