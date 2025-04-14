package com.example.doan.view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;

public class View_Holder_Receipt_User extends RecyclerView.ViewHolder {

    public TextView tvNameFood;
    public TextView tvNoteFood;
    public TextView tvPriceFood;
    public ImageView imgItemFood;
    public ImageView imgDown;
    public TextView tvQuantityFood;
    public ImageView imgUp;

    public View_Holder_Receipt_User(@NonNull View itemView) {
        super(itemView);

        tvNameFood = (TextView) itemView.findViewById(R.id.tv_nameFood);
        tvNoteFood = (TextView) itemView.findViewById(R.id.tv_noteFood);
        imgItemFood = (ImageView) itemView.findViewById(R.id.img_itemFood);
        tvPriceFood = (TextView) itemView.findViewById(R.id.tv_priceFood);
        imgDown = (ImageView) itemView.findViewById(R.id.img_down);
        tvQuantityFood = (TextView) itemView.findViewById(R.id.tv_quantityFood);
        imgUp = (ImageView) itemView.findViewById(R.id.img_up);
    }
}
