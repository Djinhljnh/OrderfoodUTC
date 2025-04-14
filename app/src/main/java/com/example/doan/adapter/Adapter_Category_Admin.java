package com.example.doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.model.TheLoai;
import com.example.doan.view_holder.View_Holder_Category_Admin;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Category_Admin extends RecyclerView.Adapter<View_Holder_Category_Admin> {
    private List<TheLoai> theLoaiList;
    private Callback callback;

    public Adapter_Category_Admin(List<TheLoai> theLoaiList, Callback callback) {
        this.theLoaiList = theLoaiList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public View_Holder_Category_Admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_category_admin, parent, false);

        View_Holder_Category_Admin viewHolderCategoryAdmin = new View_Holder_Category_Admin(row);
        return viewHolderCategoryAdmin;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Category_Admin holder, int position) {
        TheLoai theLoai = theLoaiList.get(position);

        holder.tvNameCategory.setText(theLoai.getId()+". "+theLoai.getName_category());
        Picasso.get().load(theLoai.getImg_category()).into(holder.imgCategory);
        holder.imgUpdate.setOnClickListener(v ->{
            callback.update(theLoai);
        });
        holder.imgDelete.setOnClickListener(v -> {
            callback.delete(theLoai);
        });
    }

    @Override
    public int getItemCount() {
        return theLoaiList == null ? 0 : theLoaiList.size();
    }

    public  interface Callback{
        void update(TheLoai theLoai);
        void delete(TheLoai theLoai);
    }

    public void filterList(ArrayList<TheLoai> filteredList) {
        theLoaiList = filteredList;
        notifyDataSetChanged();
    }
}
