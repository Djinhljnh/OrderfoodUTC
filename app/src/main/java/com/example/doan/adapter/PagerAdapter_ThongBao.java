package com.example.doan.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.activities.ThongBao;

public class PagerAdapter_ThongBao extends RecyclerView.Adapter<PagerAdapter_ThongBao.ViewHolder> {
    int[] images;

    public PagerAdapter_ThongBao(int[] images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_thongbao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img_thongbao.setBackgroundResource(images[position]);
        holder.img_thongbao.setOnClickListener(v ->{
            Intent intent = new Intent(v.getContext(), ThongBao.class);
            Bundle bundle = new Bundle();
            bundle.putInt("image", images[position]);
            intent.putExtra("bundle", bundle);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_thongbao;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_thongbao = itemView.findViewById(R.id.img_thongbao);
        }
    }

}
