package com.example.doan.model;

public class HoaDon {
    private int id;
    private String img_Food;
    private String name_Food;
    private String price_Food;
    private String quantity_Food;
    private String note_Food;

    public HoaDon() {
    }

    public HoaDon(int id, String img_Food, String name_Food, String price_Food, String quantity_Food, String note_Food) {
        this.id = id;
        this.img_Food = img_Food;
        this.name_Food = name_Food;
        this.price_Food = price_Food;
        this.quantity_Food = quantity_Food;
        this.note_Food = note_Food;
    }

    public String getImg_Food() {
        return img_Food;
    }

    public void setImg_Food(String img_Food) {
        this.img_Food = img_Food;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice_Food() {
        return price_Food;
    }

    public void setPrice_Food(String price_Food) {
        this.price_Food = price_Food;
    }

    public String getName_Food() {
        return name_Food;
    }

    public void setName_Food(String name_Food) {
        this.name_Food = name_Food;
    }

    public String getNote_Food() {
        return note_Food;
    }

    public void setNote_Food(String note_Food) {
        this.note_Food = note_Food;
    }

    public String getQuantity_Food() {
        return quantity_Food;
    }

    public void setQuantity_Food(String quantity_Food) {
        this.quantity_Food = quantity_Food;
    }
}
