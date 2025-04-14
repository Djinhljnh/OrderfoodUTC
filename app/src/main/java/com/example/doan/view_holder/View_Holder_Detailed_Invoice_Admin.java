package com.example.doan.view_holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;

public class View_Holder_Detailed_Invoice_Admin extends RecyclerView.ViewHolder {
    public TextView tvDate;
    public TextView tvPrice;
    public LinearLayout line_item;
    public LinearLayout line_item1;
    public TextView tvStatus;

    public View_Holder_Detailed_Invoice_Admin(@NonNull View itemView) {
        super(itemView);

        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
        line_item = itemView.findViewById(R.id.line_item);
        line_item1 = itemView.findViewById(R.id.line_item1);
    }
}
