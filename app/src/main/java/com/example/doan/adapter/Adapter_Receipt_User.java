package com.example.doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.model.HoaDon;
import com.example.doan.view_holder.View_Holder_Receipt_User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Receipt_User extends RecyclerView.Adapter<View_Holder_Receipt_User> {
    private List<HoaDon> hoaDonList;
    private Callback callback;

    public Adapter_Receipt_User(List<HoaDon> hoaDonList, Callback callback) {
        this.hoaDonList = hoaDonList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public View_Holder_Receipt_User onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_receipt_user, parent, false);

        View_Holder_Receipt_User viewHolderReceiptUser = new View_Holder_Receipt_User(row);
        return viewHolderReceiptUser;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Receipt_User holder, int position) {
        HoaDon hoaDon = hoaDonList.get(position);

        holder.tvNameFood.setText(hoaDon.getName_Food());
        holder.tvNoteFood.setText(hoaDon.getNote_Food());
        holder.tvPriceFood.setText(hoaDon.getPrice_Food());
        holder.tvQuantityFood.setText(hoaDon.getQuantity_Food());
        Picasso.get().load(hoaDon.getImg_Food()).into(holder.imgItemFood);

        holder.imgUp.setOnClickListener(v ->{
            callback.up(hoaDon);
        });
        holder.imgDown.setOnClickListener(v ->{
            callback.down(hoaDon);
        });
    }

    @Override
    public int getItemCount() {
        return hoaDonList == null ? 0 : hoaDonList.size();
    }

    public  interface Callback{
        void up(HoaDon hoaDon);
        void down(HoaDon hoaDon);
    }
}
