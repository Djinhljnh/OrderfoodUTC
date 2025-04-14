package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.adapter.Adapter_Favorite;
import com.example.doan.model.SanPham_Favorite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YeuThichUser extends AppCompatActivity {
    private SanPham_Favorite sanPham_favorite;
    private List<SanPham_Favorite> sanPham_favoriteList;
    private Adapter_Favorite adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeu_thich_user);

        recyclerView = findViewById(R.id.rcv_favorite);
        sanPham_favoriteList = new ArrayList<>();
        adapter = new Adapter_Favorite(sanPham_favoriteList);
        getList();

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

    private void getList(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    sanPham_favorite = dataSnapshot.getValue(SanPham_Favorite.class);
                    sanPham_favoriteList.add(sanPham_favorite);
                }
                Collections.reverse(sanPham_favoriteList);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(YeuThichUser.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });
    }
}