package com.example.doan.view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;

public class View_Holder_Food_HoaDonChiTiet extends RecyclerView.ViewHolder {
    public TextView tvNameFood;
    public TextView tvNoteFood;
    public TextView tvQuantityFood;

    public View_Holder_Food_HoaDonChiTiet(@NonNull View itemView) {
        super(itemView);

        tvNameFood = (TextView) itemView.findViewById(R.id.tv_nameFood);
        tvNoteFood = (TextView) itemView.findViewById(R.id.tv_noteFood);
        tvQuantityFood = (TextView) itemView.findViewById(R.id.tv_quantityFood);
    }
}
