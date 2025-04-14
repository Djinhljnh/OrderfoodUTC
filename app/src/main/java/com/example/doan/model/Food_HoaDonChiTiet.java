package com.example.doan.model;

public class Food_HoaDonChiTiet {
    private String name;
    private String quantity;
    private String note;

    public Food_HoaDonChiTiet() {
    }

    public Food_HoaDonChiTiet(String name, String quantity, String note) {
        this.name = name;
        this.quantity = quantity;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
