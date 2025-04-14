package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.example.doan.R;
import com.example.doan.adapter.PagerAdapter_GioHang;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GioHangUser extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private PagerAdapter_GioHang pagerAdapterGioHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang_user);

        tabLayout = findViewById(R.id.tablayout_giohang);
        viewPager2 = findViewById(R.id.viewpager2_giohang);
        pagerAdapterGioHang = new PagerAdapter_GioHang(this);
        viewPager2.setAdapter(pagerAdapterGioHang);

        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                if (position == 0) {
                    tab.setText("Cart");
                } else {
                    tab.setText("Oder");
                }
            }
        });
        mediator.attach();

        findViewById(R.id.img_trangchu).setOnClickListener(v ->{
            Intent intent = new Intent(this, TrangChuUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
        findViewById(R.id.img_tintuc).setOnClickListener(v ->{
            Intent intent = new Intent(this, YeuThichUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
        findViewById(R.id.img_giohang).setOnClickListener(v ->{
            Intent intent = new Intent(this, GioHangUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
        findViewById(R.id.img_thongtin).setOnClickListener(v ->{
            Intent intent = new Intent(this, ThongTinUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
    }
}