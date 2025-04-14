package com.example.doan.view_holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;

public class View_Holder_Detailed_Invoice_User extends RecyclerView.ViewHolder {
    public TextView tvDate;
    public TextView tvPrice;
    public LinearLayout line_item;

    public View_Holder_Detailed_Invoice_User(@NonNull View itemView) {
        super(itemView);

        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        line_item = itemView.findViewById(R.id.line_item);
    }
}
