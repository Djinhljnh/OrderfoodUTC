package com.example.doan.view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;

public class View_Holder_Food_Admin extends RecyclerView.ViewHolder {
    public LinearLayout lineItemFood;
    public ImageView imgItemFood;
    public TextView tvNameFood;
    public TextView tvPriceFood;
    public TextView tvCategoryFood;
    public TextView tvNoteFood;
    public ImageView btnEdit;
    public ImageView btnDelete;

    public View_Holder_Food_Admin(@NonNull View itemView) {
        super(itemView);
        lineItemFood = (LinearLayout) itemView.findViewById(R.id.line_itemFood);
        imgItemFood = (ImageView) itemView.findViewById(R.id.img_itemFood);
        tvNameFood = (TextView) itemView.findViewById(R.id.tv_nameFood);
        tvPriceFood = (TextView) itemView.findViewById(R.id.tv_priceFood);
        tvCategoryFood = (TextView) itemView.findViewById(R.id.tv_categoryFood);
        tvNoteFood = (TextView) itemView.findViewById(R.id.tv_noteFood);
        btnEdit = (ImageView) itemView.findViewById(R.id.btnEdit);
        btnDelete = (ImageView) itemView.findViewById(R.id.btnDelete);

    }
}
