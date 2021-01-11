package com.example.fitshare;


import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.fitshare.model.ModelList;
import com.example.fitshare.model.ModelProduct;
import com.example.fitshare.model.Products;
import com.example.fitshare.model.myLists;

import java.util.ArrayList;
import java.util.List;


public class ProductsFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    HomeActivity parent;
    List<Products> Products_list = new ArrayList<>();
    EditText newListName_edit;
    ProductsAdapter ProductsAdapter;
    RecyclerView listView;
    String amount_popup = "יח׳";
    ProductsViewModel viewModel;
    androidx.lifecycle.LiveData<List<Products>> LiveData;


    //dialog parameter
    EditText amount_edit;
    RecyclerView myrcylist;
    Button cancel_btn;
    Button popup_btn;
    Button amount_add_btn;
    Button amount_remove_btn;

    private SwipeRefreshLayout swipeRefreshLayout;
    private MyListAdapter MyListAdapterProduct;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

    }


    public ProductsFragment() {

    }


    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_products, container, false);


        //put name on toolbar
        parent.setTitle(parent.ListName);
        Log.d("TAG", "onCreateView:" + parent.ListName);

        parent.save_btn.setVisibility(View.INVISIBLE);
        parent.logout_btn.setVisibility(View.INVISIBLE);
        parent.addList_btn.setVisibility(View.VISIBLE);
        parent.copy_list_btn.setVisibility(View.VISIBLE);
        parent.language_btn.setVisibility(View.INVISIBLE);
        parent.floating_addList_btn.hide();
        parent.addList_btn.setBackgroundResource(R.drawable.editicon);

        LiveData = viewModel.getData(parent.listID);
        LiveData.observe(getViewLifecycleOwner(), new Observer<List<Products>>() {
            @Override
            public void onChanged(List<com.example.fitshare.model.Products> products) {
                Products_list = products;
                ProductsAdapter.setList(Products_list);

            }
        });
        parent.copy_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCopyProductDialog();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        listView = view.findViewById(R.id.Products_recycler);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManger = new LinearLayoutManager(parent);
        listView.setLayoutManager(layoutManger);
        new ItemTouchHelper(ItemTouchHelperCallback).attachToRecyclerView(listView);

        ProductsAdapter = new ProductsAdapter(parent);
        listView.setAdapter((RecyclerView.Adapter) ProductsAdapter);
        ProductsAdapter.setonItemClickListenr(new ProductsAdapter.onItemClickListenr() {
            @Override
            public void onClick(int position) {

                Products products = Products_list.get(position);
                openAmountProductDialog(products);


            }
        });

        newListName_edit = view.findViewById(R.id.newListName_edit);

        if (parent.value.getLanguage().equals("English"))
            newListName_edit.setHint("Add a new item..");


        newListName_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        newListName_edit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            addProductwithEnter();

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        return view;


    }


    //Placing the possibility of skidding in the circular list

    ItemTouchHelper.SimpleCallback ItemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


            Button yes_btn;
            Button cancel_btn;
            TextView delete_txt;

            Dialog dialog1 = new Dialog(parent);
            dialog1.setContentView(R.layout.delete_dialog);
            dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Window window = dialog1.getWindow();
            window.setGravity(Gravity.CENTER);

            cancel_btn = dialog1.findViewById(R.id.cancel_btn);
            yes_btn = dialog1.findViewById(R.id.yes_btn);
            delete_txt = dialog1.findViewById(R.id.delete_txt);


            if (parent.value.getLanguage().equals("English")) {
                yes_btn.setText("yes");
                delete_txt.setText("Are you sure you want to delete?");
            }

            yes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Products products = Products_list.get(viewHolder.getAdapterPosition());
                    ModelProduct.instance.deleteProducts(products, viewHolder.getAdapterPosition(), new ModelProduct.deleteProductsListener() {
                        @Override
                        public void onComplete() {
                            dialog1.cancel();
                        }
                    });

                }
            });
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductsAdapter.notifyDataSetChanged();
                    dialog1.cancel();
                }
            });


            dialog1.setCancelable(false);
            window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog1.show();


        }
    };

    public void openCopyProductDialog() {


        Dialog dialog1 = new Dialog(parent);

        dialog1.setContentView(R.layout.copy_dialog);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_3;
        Window window = dialog1.getWindow();
        window.setGravity(Gravity.CENTER);

        myrcylist = dialog1.findViewById(R.id.myrcylist);

        TextView copy_txt = dialog1.findViewById(R.id.copy_txt);

        if (parent.value.getLanguage().equals("English"))
            copy_txt.setText("Which list to copy?");

        myrcylist.setHasFixedSize(true);
        LinearLayoutManager layoutManger = new LinearLayoutManager(parent);
        myrcylist.setLayoutManager(layoutManger);

        MyListAdapterProduct = new MyListAdapter(parent);
        List<myLists> myLists1 = new ArrayList<>();


        myLists1.addAll(parent.AllMyLists);
        myLists1.remove(parent.ListPosition);
        MyListAdapterProduct.setList(myLists1);
        myrcylist.setAdapter((RecyclerView.Adapter) MyListAdapterProduct);

        MyListAdapterProduct.setonItemClickListenr(new MyListAdapter.onItemClickListenr() {
            @Override
            public void onClick(int position) {

                myLists myLists = myLists1.get(position);
                int i = parent.getListPosition(myLists);
                ModelProduct.instance.CopyProducts(myLists.getListID(), i, new ModelList.updateMyListsListener() {
                    @Override
                    public void onComplete() {
                        dialog1.cancel();
                    }
                });

            }
        });


        cancel_btn = dialog1.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductsAdapter.notifyDataSetChanged();
                dialog1.cancel();
            }
        });


        dialog1.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog1.show();


    }

    public void openAmountProductDialog(Products products) {


        Dialog dialog1 = new Dialog(parent);
        dialog1.setContentView(R.layout.amount_dialog);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        Window window = dialog1.getWindow();
        window.setGravity(Gravity.CENTER);

        popup_btn = dialog1.findViewById(R.id.popup_btn);
        amount_add_btn = dialog1.findViewById(R.id.amount_add_btn);
        amount_add_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String value = amount_edit.getText().toString();
                int finalValue = Integer.parseInt(value);
                finalValue++;
                amount_edit.setError(null);
                amount_edit.setText(String.valueOf(finalValue));
            }
        });
        amount_remove_btn = dialog1.findViewById(R.id.amount_remove_btn);
        amount_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = amount_edit.getText().toString();
                int finalValue = Integer.parseInt(value);
                if (finalValue != 1) {
                    finalValue--;
                    amount_edit.setText(String.valueOf(finalValue));
                    amount_edit.setError(null);
                } else {
                    if (parent.value.getLanguage().equals("English")) {
                        amount_edit.setError("Minimum");

                    } else
                        amount_edit.setError("הגעת למינימום");
                }

            }
        });
        amount_edit = dialog1.findViewById(R.id.amount_edit);
        String[] amountArr = products.getAmount().split(" ");
        String amount = amountArr[0];
        amount_edit.setText(amount);
        if (parent.value.getLanguage().equals("Hebrew")) {
            if (amountArr[1].equals("Units"))
                popup_btn.setText("יח׳");
            else if (amountArr[1].equals("Gm"))
                popup_btn.setText("גרם");
            else popup_btn.setText("קילוגרם");

        } else {
            popup_btn.setText(amountArr[1]);
        }
        amount_popup = amountArr[1];

        if (amount_popup.equals("Units")) {
            amount_edit.setEnabled(false);
            amount_add_btn.setVisibility(View.VISIBLE);
            amount_remove_btn.setVisibility(View.VISIBLE);
        } else {
            amount_add_btn.setVisibility(View.INVISIBLE);
            amount_remove_btn.setVisibility(View.INVISIBLE);
            amount_edit.setEnabled(true);
        }
        popup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });


        Button save_btn = dialog1.findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String[] amountArr = amount_edit.getText().toString().split(" ");
                String amount = amountArr[0];
                if (TextUtils.isEmpty(amount)) {
                    amount_edit.setText("1");
                    amount_popup = "Units";
                    if (parent.value.getLanguage().equals("English"))
                        popup_btn.setText("Units");

                    else popup_btn.setText("יח׳");

                    return;
                }


                String new_amount = amount + " " + amount_popup;
                products.setAmount(new_amount);
                ModelProduct.instance.updateProducts(products, null);
                dialog1.cancel();
            }
        });

        Button cancel_btn = dialog1.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });

        dialog1.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog1.show();


    }

    //popup of amount of product
    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(parent, view);
        popup.setOnMenuItemClickListener(this);
        if (parent.value.getLanguage().equals("Hebrew"))
            popup.inflate(R.menu.product_dimensions_popup_hebrew);
        else popup.inflate(R.menu.product_dimensions_popup_english);
        popup.show();
    }

    //Set the click item
    @Override
    public boolean onMenuItemClick(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.unitsH:
                popup_btn.setText("יח׳");
                amount_popup = "Units";

                break;
            case R.id.gramH:
                popup_btn.setText("גרם");
                amount_popup = "Gm";
                break;


            case R.id.kilogramH:
                popup_btn.setText("קילוגרם");
                amount_popup = "Kg";
                break;

            case R.id.unitsE:
                popup_btn.setText("Units");
                amount_popup = "Units";

                break;
            case R.id.gramE:
                popup_btn.setText("Gm");
                amount_popup = "Gm";
                break;


            case R.id.kilogramE:
                popup_btn.setText("Kg");
                amount_popup = "Kg";
                break;
            default:

                return false;
        }


        if (amount_popup.equals("Units")) {
            amount_edit.setEnabled(false);
            amount_add_btn.setVisibility(View.VISIBLE);
            amount_remove_btn.setVisibility(View.VISIBLE);
        } else {
            amount_edit.setEnabled(true);
            amount_add_btn.setVisibility(View.INVISIBLE);
            amount_remove_btn.setVisibility(View.INVISIBLE);
        }
        return true;
    }


    public void addProductwithEnter() {
        String listName = newListName_edit.getText().toString().trim();
        Products Products;
        if (parent.value.getLanguage().equals("Hebrew")) {
            if (TextUtils.isEmpty(listName)) {
                newListName_edit.setError("שם פריט?");
                return;
            }
            for (Products name : Products_list) {
                if (name.getName().equals(listName)) {
                    newListName_edit.setError("הפריט כבר קיים");
                    return;
                }
            }
        } else {
            if (TextUtils.isEmpty(listName)) {
                newListName_edit.setError("Item name?");
                return;
            }
            for (Products name : Products_list) {
                if (name.getName().equals(listName)) {
                    newListName_edit.setError("The item already exists");
                    return;
                }
            }
        }
        Products = new Products(listName, false, "noImage", "1 Units", parent.listID);
        ModelProduct.instance.addProducts(Products, null);

        Products_list.add(Products);
        ProductsAdapter.setList(Products_list);
        newListName_edit.getText().clear();
        newListName_edit.requestFocus();
        newListName_edit.getText().clear();
        newListName_edit.requestFocus();

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) parent.getSystemService(parent.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        viewModel.Refresh(parent.listID, new ProductsViewModel.RefreshProductsListener() {
            @Override
            public void onComplete() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}