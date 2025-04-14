package com.example.doan.model;

import androidx.annotation.NonNull;

public class TheLoai {
    private int id;
    private String img_category;
    private String name_category;

    public TheLoai() {
    }

    public TheLoai(int id, String img_category, String name_category) {
        this.id = id;
        this.img_category = img_category;
        this.name_category = name_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg_category() {
        return img_category;
    }

    public void setImg_category(String img_category) {
        this.img_category = img_category;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    @NonNull
    @Override
    public String toString(){
        return getName_category();
    }
}
