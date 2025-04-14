package com.example.doan.view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;

public class View_Holder_Favorite extends RecyclerView.ViewHolder {
    public LinearLayout lineItemFood;
    public ImageView imgItemFood;
    public TextView tvNameFood;
    public TextView tvNoteFood;
    public ImageView imgFavorite;

    public View_Holder_Favorite(@NonNull View itemView) {
        super(itemView);
        lineItemFood = (LinearLayout) itemView.findViewById(R.id.line_itemFood);
        imgItemFood = (ImageView) itemView.findViewById(R.id.img_itemFood);
        tvNameFood = (TextView) itemView.findViewById(R.id.tv_nameFood);
        tvNoteFood = (TextView) itemView.findViewById(R.id.tv_noteFood);
        imgFavorite = (ImageView) itemView.findViewById(R.id.img_favorite);
    }
}
