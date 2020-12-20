package com.example.fitshare;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
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

import com.example.fitshare.model.ImageModel;
import com.example.fitshare.model.ModelFirebase;
import com.example.fitshare.model.myLists;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView listView;
    HomeActivity parent;
    AddListAdapter addListAdapter;
    EditText listName_edit;

    //Dialog parameter
    String userName;

    List<String> user = new ArrayList<>();

    public AddListFragment() {
        // Required empty public constructor
    }


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
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent = (HomeActivity) getActivity();
        ModelFirebase.instance.setActivity(parent);
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

        if (parent.value.language.equals("Hebrew")) {
            listName_txt.setText(R.string.listNameHebrew);
            listFriend_txt.setText(R.string.listFriendHebrew);
            add_friend_btn.setText("הוסף חבר חדש");

        } else {
            listName_txt.setText(R.string.listNameEnglish);
            listFriend_txt.setText(R.string.listFriendEnglish);
            add_friend_btn.setText("Add new friend");
        }
        parent.save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String listName = listName_edit.getText().toString().trim();
                if (parent.value.language.equals("Hebrew")) {
                    if (TextUtils.isEmpty(listName)) {
                        listName_edit.setError("צריך שם לרשימה");
                        return;


                    }
                    for (myLists name : parent.value.getMyLists()) {
                        if (name.ListName.equals(listName)) {
                            if (parent.CorrectList != null)
                                if (parent.CorrectList.ListName.equals(listName))
                                    break;
                            listName_edit.setError("השם תפוס");
                            return;
                        }
                    }
                }
                else
                {
                    if (TextUtils.isEmpty(listName)) {
                        listName_edit.setError("Need a name for the list");
                        return;


                    }
                    for (myLists name : parent.value.getMyLists()) {
                        if (name.ListName.equals(listName)) {
                            if (parent.CorrectList != null)
                                if (parent.CorrectList.ListName.equals(listName))
                                    break;
                            listName_edit.setError("The name is busy");
                            return;
                        }
                    }

                }
                    if (parent.CorrectList != null) {
                        if (!parent.CorrectList.ListName.equals(listName))
                            parent.CorrectList.ListName = listName;
                        ModelFirebase.instance.UpdateListData(parent.CorrectList, parent.ListPosition);
                        for (String email : user) {
                            parent.addUserToList(email);
                        }


                    } else {
                        ModelFirebase.instance.newList(listName, parent.value.id, user);
                    }
                hideKeyboard(v);
                    parent.navCtrl.popBackStack();


                }


        });
        listName_edit = view.findViewById(R.id.listName_edit);
        listName_edit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

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
            }
        });


        add_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText user_edit;

                Dialog dialog1 = new Dialog(parent);

                dialog1.setContentView(R.layout.add_user);
                dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Window window = dialog1.getWindow();
                window.setGravity(Gravity.CENTER);

                user_edit = dialog1.findViewById(R.id.user_name_edit);
                Button cancel_btn = dialog1.findViewById(R.id.cancel_btn);

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.cancel();
                    }
                });
                Button ok_btn = dialog1.findViewById(R.id.ok_btn);
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userName = user_edit.getText().toString().trim();
                        if (parent.value.language.equals("Hebrew")) {
                            if (TextUtils.isEmpty(userName)) {
                                user_edit.setError("צריך שם משתמש");
                                return;
                            }
                            if (parent.value.email.equals(userName)) {
                                user_edit.setError("אתה לא יכול להוסיף את עצמך!");
                                return;
                            }

                            if (ModelFirebase.instance.CheckUserExists(userName)) {
                                if (user.contains(userName)) {
                                    user_edit.setError("משתמש כבר קיים ברשימה");
                                    return;
                                }
                                parent.setUserDailog(userName);


                                // }
                                if (parent.CorrectList != null) {
                                    user.add(userName);
                                    addListAdapter.notifyDataSetChanged();
                                    addListAdapter.updateList(user);
                                    listView.setAdapter((RecyclerView.Adapter) addListAdapter);
                                }
                                dialog1.cancel();
                            } else {
                                user_edit.setError("משתמש לא קיים");
                                return;
                            }
                        }
                        else {
                            if (TextUtils.isEmpty(userName)) {
                                user_edit.setError("Need a username");
                                return;
                            }
                            if (parent.value.email.equals(userName)) {
                                user_edit.setError("You can not add yourself!");
                                return;
                            }

                            if (ModelFirebase.instance.CheckUserExists(userName)) {
                                if (user.contains(userName)) {
                                    user_edit.setError("A user already exists in the list");
                                    return;
                                }
                                parent.setUserDailog(userName);


                                // }
                                if (parent.CorrectList != null) {
                                    user.add(userName);
                                    addListAdapter.notifyDataSetChanged();
                                    addListAdapter.updateList(user);
                                    listView.setAdapter((RecyclerView.Adapter) addListAdapter);
                                }
                                dialog1.cancel();
                            } else {
                                user_edit.setError("User does not exist");
                                return;
                            }
                        }

                    }

                });


                dialog1.setCancelable(true);
                window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                dialog1.show();

            }
        });
        listView = view.findViewById(R.id.friend_ryc);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManger = new LinearLayoutManager(parent);
        listView.setLayoutManager(layoutManger);
        EditText List_name = view.findViewById(R.id.listName_edit);
        if (parent.CorrectList != null) {
            List_name.setText(parent.CorrectList.ListName);
            user = ModelFirebase.instance.getUserList();
        } else {
            user = parent.userDailog;

        }
        addListAdapter = new AddListAdapter(parent, user);

        listView.setAdapter((RecyclerView.Adapter) addListAdapter);
        addListAdapter.setonItemClickListenr(new AddListAdapter.onItemClickListenr() {
            @Override
            public void onClick(int position) {


            }
        });

        return view;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) parent.getSystemService(parent.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}