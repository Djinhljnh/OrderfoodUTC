package com.example.doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.model.HoaDonChiTiet;

import java.util.List;

import android.widget.TextView;


import com.example.doan.R;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.HoaDonViewHolder> {

    private List<HoaDonChiTiet> hoaDonList;

    public HoaDonAdapter(List<HoaDonChiTiet> hoaDonList) {
        this.hoaDonList = hoaDonList;
    }

    @NonNull
    @Override
    public HoaDonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hoadon, parent, false);
        return new HoaDonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonViewHolder holder, int position) {
        HoaDonChiTiet hoaDon = hoaDonList.get(position);
        holder.tvId.setText("ID: " + hoaDon.getId());
        holder.tvDate.setText("Date: " + hoaDon.getDate());
        holder.tvTotal.setText("Total: $" + hoaDon.getSum_Price());
    }

    @Override
    public int getItemCount() {
        return hoaDonList.size();
    }

    public static class HoaDonViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvDate, tvTotal;

        public HoaDonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_invoice_id);
            tvDate = itemView.findViewById(R.id.tv_invoice_date);
            tvTotal = itemView.findViewById(R.id.tv_invoice_total);
        }
    }
}
