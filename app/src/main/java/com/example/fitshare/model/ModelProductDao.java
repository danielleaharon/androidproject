package com.example.fitshare.model;

import android.os.AsyncTask;

import java.util.List;

public class ModelProductDao {
    public final static ModelProductDao instance = new ModelProductDao();
    private ModelProductDao(){

    }

    public interface GetAllProductsListener{
        void onComplete(List<Products> data);
    }

    public void getAllProducts(String listID, final GetAllProductsListener listener){
        class MyAsyncTask extends AsyncTask {
            List<Products> data;
            @Override
            protected Object doInBackground(Object[] objects) {
                data = AppLocalProductDb.db.ProductsDao().getAllProducts(listID);
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                listener.onComplete(data);
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public interface AddProductsListener{
        void onComplete();
    }
    public void addProducts(final Products Products, final ModelProductDao.AddProductsListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalProductDb.db.ProductsDao().insertAll(Products);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
    public interface deleteProductsListener{
        void onComplete();
    }
    public void deleteProducts(final Products Products, final ModelProductDao.deleteProductsListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalProductDb.db.ProductsDao().delete(Products);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public void updateProducts(final Products products, final ModelListDao.updateMyListsListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalProductDb.db.ProductsDao().updateProduct(products);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}
