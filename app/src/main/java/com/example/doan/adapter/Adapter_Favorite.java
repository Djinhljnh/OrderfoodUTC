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
import com.example.doan.model.SanPham_Favorite;
import com.example.doan.view_holder.View_Holder_Favorite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Favorite extends RecyclerView.Adapter<View_Holder_Favorite> {
    private List<SanPham_Favorite> sanPham_favoritesList;

    public Adapter_Favorite(List<SanPham_Favorite> sanPham_favoritesList) {
        this.sanPham_favoritesList = sanPham_favoritesList;
    }

    @NonNull
    @Override
    public View_Holder_Favorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_food_favorite, parent, false);

        View_Holder_Favorite viewHolderFavorite = new View_Holder_Favorite(row);
        return viewHolderFavorite;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Favorite holder, int position) {
        SanPham_Favorite sanPham = sanPham_favoritesList.get(position);

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
        holder.imgFavorite.setOnClickListener(v ->{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Users");
            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Favorite")
                    .child(sanPham.getName_product()).removeValue();
            sanPham_favoritesList.clear();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return sanPham_favoritesList == null ? 0 : sanPham_favoritesList.size();
    }
}
