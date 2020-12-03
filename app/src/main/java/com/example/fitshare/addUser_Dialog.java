package com.example.fitshare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class addUser_Dialog extends AppCompatDialogFragment {
    HomeActivity parent;
    EditText user_edit;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_user,null);
        user_edit= view.findViewById(R.id.user_name_edit);
        builder.setView(view).setTitle("add user").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        }).setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String userName = user_edit.getText().toString().trim();
                if(TextUtils.isEmpty(userName))
                {
                    user_edit.setError("need UserName");
                    return;
                }
                parent.addUserToList(userName);
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
