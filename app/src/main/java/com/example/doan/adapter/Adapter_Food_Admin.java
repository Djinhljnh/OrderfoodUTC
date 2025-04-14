package com.example.doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.model.SanPham;
import com.example.doan.model.SanPham_Popular;
import com.example.doan.view_holder.View_Holder_Food_Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Food_Admin extends RecyclerView.Adapter<View_Holder_Food_Admin> {
    private List<SanPham> sanPhamList;
    private Callback callback;

    public Adapter_Food_Admin(List<SanPham> sanPhamList, Callback callback) {
        this.sanPhamList = sanPhamList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public View_Holder_Food_Admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_food_admin, parent, false);

        View_Holder_Food_Admin viewHolderFoodAdmin = new View_Holder_Food_Admin(row);
        return viewHolderFoodAdmin;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Food_Admin holder, int position) {
        SanPham sanPham = sanPhamList.get(position);

        holder.tvNameFood.setText(sanPham.getId()+". "+sanPham.getName_product());
        holder.tvPriceFood.setText(sanPham.getPrice_product());
        holder.tvCategoryFood.setText(sanPham.getCategory());
        holder.tvNoteFood.setText(sanPham.getNote_product());
        Picasso.get().load(sanPham.getImg_product()).into(holder.imgItemFood);
        holder.imgItemFood.setOnClickListener(v ->{

            SanPham_Popular sanPham_popular = new SanPham_Popular(sanPham.getImg_product(),sanPham.getName_product(), sanPham.getPrice_product(), sanPham.getNote_product());
            FirebaseDatabase.getInstance().getReference("FoodPopulars")
                    .child(sanPham.getName_product())
                    .setValue(sanPham_popular).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(v.getContext(), "Add food popular successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
        holder.btnEdit.setOnClickListener(v ->{
            callback.update(sanPham);
        });
        holder.btnDelete.setOnClickListener(v ->{
            callback.delete(sanPham);
        });
        holder.lineItemFood.setOnLongClickListener(v ->{
            callback.update(sanPham);
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return sanPhamList == null ? 0 : sanPhamList.size();
    }

    public  interface Callback{
        void update(SanPham sanPham);
        void delete(SanPham sanPham);
    }

    public void filterList(ArrayList<SanPham> filteredList) {
        sanPhamList = filteredList;
        notifyDataSetChanged();
    }
}
