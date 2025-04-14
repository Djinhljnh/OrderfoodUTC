package com.example.doan.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.activities.HoaDonUser;
import com.example.doan.R;
import com.example.doan.model.SanPham;
import com.example.doan.view_holder.View_Holder_Food_User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Food_User extends RecyclerView.Adapter<View_Holder_Food_User> {
    private List<SanPham> sanPhamList;

    public Adapter_Food_User(List<SanPham> sanPhamList) {
        this.sanPhamList = sanPhamList;
    }

    @NonNull
    @Override
    public View_Holder_Food_User onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_food_user, parent, false);

        View_Holder_Food_User viewHolderFoodUser = new View_Holder_Food_User(row);
        return viewHolderFoodUser;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Food_User holder, int position) {
        SanPham sanPham = sanPhamList.get(position);

        holder.tvNameFood.setText(sanPham.getName_product());
        holder.tvNoteFood.setText(sanPham.getNote_product());
        Picasso.get().load(sanPham.getImg_product()).into(holder.imgItemFood);
        holder.lineItemFood.setOnClickListener(v ->{
            Intent myIntent = new Intent(v.getContext(), HoaDonUser.class);
            Bundle bundle = new Bundle();
            bundle.putString("nameFood", sanPham.getName_product());
            bundle.putString("noteFood", sanPham.getNote_product());
            bundle.putString("priceFood", sanPham.getPrice_product());
            bundle.putString("imgFood", sanPham.getImg_product());
            myIntent.putExtra("food", bundle);
            v.getContext().startActivity(myIntent);
        });
    }

    @Override
    public int getItemCount() {
        return sanPhamList == null ? 0 : sanPhamList.size();
    }

    public void filterList(ArrayList<SanPham> filteredList) {
        sanPhamList = filteredList;
        notifyDataSetChanged();
    }
}
