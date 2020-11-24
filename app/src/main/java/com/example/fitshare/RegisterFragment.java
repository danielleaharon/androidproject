package com.example.fitshare;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.fitshare.model.Proprties;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<com.example.fitshare.model.Proprties> Proprties= new ArrayList<>();
    Button Register_btn;
    EditText Name_edit_btn;
    EditText Email_edit_btn;
    EditText Password_edit_btn;
    List<Proprties> SelectedProprtieslist;
    MainActivity parent;
    private ImageView imageView;
 //   private FirebaseAuth mAuth;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent= (MainActivity) getActivity();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       // mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Register_btn=view.findViewById(R.id.Register_btn);
        Name_edit_btn=view.findViewById(R.id.Name_edit_btn);
        Email_edit_btn=view.findViewById(R.id.Email_edit_btn);
        Password_edit_btn=view.findViewById(R.id.Password_edit_btn);
        imageView = (ImageView) view.findViewById(R.id.my_avatar);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        final RecyclerView listView= view.findViewById(R.id.cheak_devices_listview);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManger=new LinearLayoutManager(parent);
        listView.setLayoutManager(layoutManger);
      //  Proprties=parent.model.getAllProprties();

       // final PropertiesAdapter PropertiesAdapter = new PropertiesAdapter(parent,Proprties);
       // listView.setAdapter((RecyclerView.Adapter) PropertiesAdapter);



        Register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  SelectedProprtieslist= PropertiesAdapter.getSelectedProprtieslist();
                StringBuilder temp = new StringBuilder();
              //  SelectedProprtieslist.forEach(z->temp.append(z.getName()+","));

                String email=Email_edit_btn.getText().toString().trim();
                String pass=Password_edit_btn.getText().toString().trim();
                String name=Name_edit_btn.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    Email_edit_btn.setError("need email");
                   // return;
                }
                if(TextUtils.isEmpty(pass))
                {
                    Password_edit_btn.setError("need pass");
                    //return;
                }
                if(TextUtils.isEmpty(name))
                {
                    Name_edit_btn.setError("need name");
                   // return;
                }
                parent.Register();
//                mAuth.createUserWithEmailAndPassword("danielle@gmail.com","12345").addOnCompleteListener(parent,new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful())
//                            Log.d("TAG","new usr: " );
//                        else Log.d("TAG","no usr " );
//
//                    }
//                });
             //   model.NewUser(Email_edit_btn.getText().toString(),Password_edit_btn.getText().toString(),Name_edit_btn.getText().toString(),SelectedProprtieslist);





            }
        });
        return view;
    }

}