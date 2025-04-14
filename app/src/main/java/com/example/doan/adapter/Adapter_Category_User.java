package com.example.doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.model.TheLoai;
import com.example.doan.view_holder.View_Holder_Category_User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Category_User extends RecyclerView.Adapter<View_Holder_Category_User> {
    private List<TheLoai> theLoaiList;
    private Callback callback;

    public Adapter_Category_User(List<TheLoai> theLoaiList, Callback callback) {
        this.theLoaiList = theLoaiList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public View_Holder_Category_User onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_category_user, parent, false);

        View_Holder_Category_User viewHolderCategoryUser = new View_Holder_Category_User(row);
        return viewHolderCategoryUser;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Category_User holder, int position) {
        TheLoai theLoai = theLoaiList.get(position);

        holder.tvNameCategory.setText(theLoai.getName_category());
        Picasso.get().load(theLoai.getImg_category()).into(holder.imgCategory);
        holder.lineItemCategory.setOnClickListener(v ->{
            callback.open(theLoai);
        });
    }

    @Override
    public int getItemCount() {
        return theLoaiList == null ? 0 : theLoaiList.size();
    }

    public  interface Callback{
        void open(TheLoai theLoai);
    }
}
