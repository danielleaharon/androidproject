package com.example.fitshare;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.fitshare.model.Products;
import com.example.fitshare.model.myLists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
     HomeActivity parent;
    List<Products> Products_list= new ArrayList<>();
    myLists mylist= new myLists();
    EditText newListName_edit;
    ProductsAdapter ProductsAdapter;
    RecyclerView listView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent= (HomeActivity) getActivity();
       // this.Products_list.clear();
        this.Products_list.addAll(parent.db.getProducts());


    }



    public ProductsFragment() {
           }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_products, container, false);
       // SortProducts();
        listView = view.findViewById(R.id.Products_recycler);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManger = new LinearLayoutManager(parent);
        listView.setLayoutManager(layoutManger);
      //  Products_list = mylist.getProductsList();
       // mylist.SortProducts();

        ProductsAdapter = new ProductsAdapter(parent, Products_list,this);
        listView.setAdapter((RecyclerView.Adapter) ProductsAdapter);



        newListName_edit = view.findViewById(R.id.newListName_edit);
        Button add_item = view.findViewById(R.id.add_list);
        add_item.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                String listName = newListName_edit.getText().toString().trim();

                if (TextUtils.isEmpty(listName)) {
                    newListName_edit.setError("need name");
                    return;
                }
                for (Products name : Products_list) {
                    if (name.name.equals(listName)) {
                        newListName_edit.setError("The name is busy");
                        return;
                    }
                }

                Products Products = new Products(listName, false);
                Products_list.add(Products);
               // SortProducts();
               // mylist.SortProducts();

                parent.db.newProducts(listName, parent.listID);

                ProductsAdapter.notifyDataSetChanged();
               ProductsAdapter.updateList(Products_list);
                listView.setAdapter((RecyclerView.Adapter) ProductsAdapter);

                //hide Keyboard
                InputMethodManager inputManager = (InputMethodManager)
                      parent.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(parent.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        return view;


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
public void SortProducts()
{
    Products_list.sort(new Comparator<Products>() {
        @Override
        public int compare(com.example.fitshare.model.Products o1, com.example.fitshare.model.Products o2) {
            if(o2.selected) return -1;
            return 1;
        }
    });

}

}