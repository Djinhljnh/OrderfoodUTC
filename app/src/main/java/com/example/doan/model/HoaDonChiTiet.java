package com.example.doan.model;

public class HoaDonChiTiet {
    private int id;
    private String date;
    private String sum_Price;
    private String name;
    private String phone;
    private String address;
    private String status;

    public HoaDonChiTiet() {
    }

    public HoaDonChiTiet(int id, String date, String sum_Price, String name, String phone, String address, String status) {
        this.id = id;
        this.date = date;
        this.sum_Price = sum_Price;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.status= status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSum_Price() {
        return sum_Price;
    }

    public void setSum_Price(String sum_Price) {
        this.sum_Price = sum_Price;
    }

}
