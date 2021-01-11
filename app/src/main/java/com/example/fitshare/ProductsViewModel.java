package com.example.fitshare;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.fitshare.model.ModelProduct;
import com.example.fitshare.model.Products;
import java.util.List;

public class ProductsViewModel extends ViewModel {
    private LiveData<List<Products>> data;


    public LiveData<List<Products>> getData(String ListID) {
        if (data == null)
            data = ModelProduct.instance.getAllProducts(ListID);
        return this.data;


    }

    public interface RefreshProductsListener {
        void onComplete();
    }

    public void Refresh(String ListID, RefreshProductsListener listener) {
        ModelProduct.instance.RefreshProducts(ListID, new ModelProduct.GetRefreshProductsListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }
}
