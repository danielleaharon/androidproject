package com.example.fitshare.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(primaryKeys = {"name", "listID"})


public class Products implements Serializable {


    @NonNull
    private String name;
    private String Amount;
    private boolean selected;
    private String imgUrl;
    private boolean isDelete;
    @NonNull
    private String listID;

    public Products(String name, boolean selected, String Url, String amount, String listID) {
        this.name = name;
        this.selected = selected;
        this.imgUrl = Url;
        this.Amount = amount;
        this.listID = listID;
        this.isDelete = false;

    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
    @NonNull
    public String getListID() {
        return this.listID;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    public String getAmount() {
        return this.Amount;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setImgUrl(String url) {
        this.imgUrl = url;
    }

    public Products() {


    }


}
