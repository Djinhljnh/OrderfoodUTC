package com.example.doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.model.Food_HoaDonChiTiet;
import com.example.doan.view_holder.View_Holder_Food_HoaDonChiTiet;

import java.util.List;

public class Adapter_Food_HoaDonChiTiet extends RecyclerView.Adapter<View_Holder_Food_HoaDonChiTiet> {
    private List<Food_HoaDonChiTiet> food_hoaDonChiTietList;

    public Adapter_Food_HoaDonChiTiet(List<Food_HoaDonChiTiet> food_hoaDonChiTietList) {
        this.food_hoaDonChiTietList = food_hoaDonChiTietList;
    }

    @NonNull
    @Override
    public View_Holder_Food_HoaDonChiTiet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_food_hoadonchitiet, parent, false);

        View_Holder_Food_HoaDonChiTiet viewHolderFoodHoaDonChiTiet = new View_Holder_Food_HoaDonChiTiet(row);
        return viewHolderFoodHoaDonChiTiet;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Food_HoaDonChiTiet holder, int position) {
        Food_HoaDonChiTiet hoaDon = food_hoaDonChiTietList.get(position);

        holder.tvNameFood.setText(hoaDon.getName());
        holder.tvNoteFood.setText(hoaDon.getNote());
        holder.tvQuantityFood.setText(hoaDon.getQuantity());
    }

    @Override
    public int getItemCount() {
        return food_hoaDonChiTietList == null ? 0 : food_hoaDonChiTietList.size();
    }
}
