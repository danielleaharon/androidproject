package com.example.fitshare.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class ModelProduct {


    public final static ModelProduct instance = new ModelProduct();
    String listID;
    List<Products> ProductsData = new ArrayList<>();

    private ModelProduct() {

    }

    public interface GetAllProductsListener {
        void onComplete(List<Products> data);
    }

    public LiveData<List<Products>> getAllProducts(String listID) {

        LiveData<List<Products>> liveData = AppLocalProductDb.db.ProductsDao().getAllProducts(listID);
        this.listID = listID;
        RefreshProducts(listID, null);
        return liveData;

    }

    public interface GetRefreshProductsListener {
        void onComplete();
    }

    public void RefreshProducts(String listID, final GetRefreshProductsListener listener) {
        ModelFirebaseProducts.instance.getListProducts(listID, new ModelFirebaseProducts.GetProductsListener() {
            @Override
            public void onComplete(List<Products> data, List<String> userList) {
                ProductsData = data;
                class MyAsyncTask extends AsyncTask {

                    @Override
                    protected Object doInBackground(Object[] objects) {
                        for (Products products : data) {
                            AppLocalProductDb.db.ProductsDao().insertAll(products);

                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if (listener != null)
                            listener.onComplete();
                    }
                }
                MyAsyncTask task = new MyAsyncTask();
                task.execute();
            }
        });

    }

    public interface AddProductsListener {
        void onComplete();
    }

    public void addProducts(final Products Products, final ModelProduct.AddProductsListener listener) {
        ModelFirebaseProducts.instance.newProducts(Products, listID, new ModelFirebaseProducts.ProductsListener() {
            @Override
            public void onComplete() {
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        AppLocalProductDb.db.ProductsDao().insertAll(Products);
                        ModelList.instance.RefreshMyList(null);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if (listener != null) {
                            listener.onComplete();
                        }
                    }
                }
                ;
                MyAsyncTask task = new MyAsyncTask();
                task.execute();
            }
        });

    }

    public interface deleteProductsListener {
        void onComplete();
    }

    public void deleteProducts(final Products Products, int position, final ModelProduct.deleteProductsListener listener) {

        ModelFirebaseProducts.instance.removeProducts(Products, position, listID, new ModelFirebaseProducts.ProductsListener() {
            @Override
            public void onComplete() {
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        AppLocalProductDb.db.ProductsDao().delete(Products);
                        ModelList.instance.RefreshMyList(null);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if (listener != null) {
                            listener.onComplete();
                        }
                    }
                }
                ;
                MyAsyncTask task = new MyAsyncTask();
                task.execute();
            }
        });

    }

    public void updateProducts(final Products products, final ModelList.updateMyListsListener listener) {
        ModelFirebaseProducts.instance.UpdateProducts(products, listID, new ModelFirebaseProducts.ProductsListener() {
            @Override
            public void onComplete() {
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        AppLocalProductDb.db.ProductsDao().updateProduct(products);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if (listener != null) {
                            listener.onComplete();
                        }
                    }
                }
                ;
                MyAsyncTask task = new MyAsyncTask();
                task.execute();
            }
        });

    }

    public void CopyProducts(final String listID, int position, final ModelList.updateMyListsListener listener) {
        ModelFirebaseProducts.instance.getCopyListData(listID, position, new ModelFirebaseProducts.CopyProductsListener() {
            @Override
            public void onComplete() {
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        ModelList.instance.RefreshMyList(null);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if (listener != null) {
                            listener.onComplete();
                        }
                    }
                }
                ;
                MyAsyncTask task = new MyAsyncTask();
                task.execute();
            }
        });

    }

    public void deleteAll() {
        AppLocalProductDb.db.ProductsDao().delete();
    }
}
