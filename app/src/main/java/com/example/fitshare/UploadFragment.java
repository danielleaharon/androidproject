package com.example.fitshare;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class UploadFragment extends Fragment {

    MainActivity parent;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent = (MainActivity) getActivity();

    }
    public UploadFragment() {
    }


    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parent.Forgot_Password_btn.setVisibility(View.INVISIBLE);
        parent.Sign_btn.setVisibility(View.INVISIBLE);
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        return view;
    }

}