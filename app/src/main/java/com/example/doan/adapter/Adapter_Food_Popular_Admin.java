package com.example.doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.model.SanPham_Popular;
import com.example.doan.view_holder.View_Holder_Food_Admin;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Food_Popular_Admin extends RecyclerView.Adapter<View_Holder_Food_Admin> {
    private List<SanPham_Popular> sanPhamList;

    public Adapter_Food_Popular_Admin(List<SanPham_Popular> sanPhamList) {
        this.sanPhamList = sanPhamList;
    }

    @NonNull
    @Override
    public View_Holder_Food_Admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_food_admin, parent, false);

        View_Holder_Food_Admin viewHolderFoodAdmin = new View_Holder_Food_Admin(row);
        return viewHolderFoodAdmin;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Food_Admin holder, int position) {
        SanPham_Popular sanPham = sanPhamList.get(position);

        holder.tvNameFood.setText(sanPham.getName_product());
        holder.tvPriceFood.setText(sanPham.getPrice_product());
        holder.tvNoteFood.setText(sanPham.getNote_product());
        Picasso.get().load(sanPham.getImg_product()).into(holder.imgItemFood);

        holder.imgItemFood.setOnClickListener(v ->{
            FirebaseDatabase.getInstance().getReference("FoodPopulars")
                    .child(sanPham.getName_product()).removeValue();
            sanPhamList.clear();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return sanPhamList == null ? 0 : sanPhamList.size();
    }

}
