package com.example.doan.view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;

public class View_Holder_Category_User extends RecyclerView.ViewHolder {
    public LinearLayout lineItemCategory;
    public ImageView imgCategory;
    public TextView tvNameCategory;

    public View_Holder_Category_User(@NonNull View itemView) {
        super(itemView);
        lineItemCategory = (LinearLayout) itemView.findViewById(R.id.line_itemCategory);
        imgCategory = (ImageView) itemView.findViewById(R.id.img_category);
        tvNameCategory = (TextView) itemView.findViewById(R.id.tv_nameCategory);
    }
}
