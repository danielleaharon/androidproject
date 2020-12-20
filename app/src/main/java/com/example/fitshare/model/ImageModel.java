package com.example.fitshare.model;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Blob;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ImageModel {

    public interface Listener {
        void onSuccess(String url);

        void onFail();
    }

    public static void uploadImage(Bitmap bitmap, String name, final Listener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = storage.getReference().child("image").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mStorageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mStorageRef.getDownloadUrl().addOnSuccessListener((uri)->{
                    Uri downloadUrl=uri;
                    listener.onSuccess(downloadUrl.toString());



                });
            }
        });
    }
    public static void deleteImage(String url)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference photoRef = storage.getReferenceFromUrl(url);

        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    public static void copyImage(String url,String name, final Listener listener)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference photoRef = storage.getReferenceFromUrl(url);
        StorageReference mStorageRefNew = storage.getReference().child("image").child(name);

        final long ONE_MEGABYTE = 1024 * 1024;
        Task<byte[]> bytes = photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                UploadTask uploadTask = mStorageRefNew.putBytes(bytes);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFail();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mStorageRefNew.getDownloadUrl().addOnSuccessListener((uri)->{
                            Uri downloadUrl=uri;
                            listener.onSuccess(downloadUrl.toString());



                        });
                    }
                });

            }
        });


    }

}
