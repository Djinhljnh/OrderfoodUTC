package com.example.doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.model.HoaDonChiTietAdmin;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {

    private List<HoaDonChiTietAdmin> list;

    public InvoiceAdapter(List<HoaDonChiTietAdmin> list) {
        this.list = list;
    }

    public void setData(List<HoaDonChiTietAdmin> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice_admin, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        HoaDonChiTietAdmin invoice = list.get(position);
        holder.tvname.setText("Nmae: " + invoice.getName());
        holder.tvDate.setText("Date: " + invoice.getDate());
        holder.tvTotal.setText("Total: $" + invoice.getSum_Price());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvname, tvDate, tvTotal;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.tv_invoice_name);
            tvDate = itemView.findViewById(R.id.tv_invoice_date);
            tvTotal = itemView.findViewById(R.id.tv_invoice_total);
        }
    }
}
