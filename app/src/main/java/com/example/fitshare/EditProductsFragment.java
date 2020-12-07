package com.example.fitshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.fitshare.model.Products;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProductsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

 HomeActivity parent;
    EditText Product_name_edit;
static final int REQUEST_IMAGE_CAPTURE=1;
final static int RESAULT_SUCCESS=0;
    Button img_btn;
    ImageView imageProducts;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProductsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent= (HomeActivity) getActivity();

    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProductsFragment newInstance(String param1, String param2) {
        EditProductsFragment fragment = new EditProductsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String products_name =EditProductsFragmentArgs.fromBundle(getArguments()).getProductName();
        int products_Unit =EditProductsFragmentArgs.fromBundle(getArguments()).getProductUnit();
        View view = inflater.inflate(R.layout.fragment_edit_products, container, false);
        Product_name_edit=view.findViewById(R.id.Product_name_edit);
       Product_name_edit.setText(products_name);
        imageProducts=view.findViewById(R.id.imageProducts);
       img_btn=view.findViewById(R.id.add_img_btn);
       img_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               takePhoto();
           }
       });
        return view;
    }

    public void takePhoto()
    {

        Intent tackPicIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(tackPicIntent.resolveActivity(getActivity().getPackageManager())!=null)
        {
            startActivityForResult(tackPicIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode== Activity.RESULT_OK)
        {
            Bundle extras= data.getExtras();
            Bitmap imageBitmap= (Bitmap) extras.get("data");
            imageProducts.setImageBitmap(imageBitmap);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}