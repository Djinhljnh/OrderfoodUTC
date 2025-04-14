package com.example.doan.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan.fragment.Frag_Cart;
import com.example.doan.fragment.Frag_Oder;

public class PagerAdapter_GioHang extends FragmentStateAdapter {
    int soluong=2;

    public PagerAdapter_GioHang(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new Frag_Cart();
            case 1: return new Frag_Oder();
            default: return new Frag_Cart();
        }
    }

    @Override
    public int getItemCount() {
        return soluong;
    }
}
