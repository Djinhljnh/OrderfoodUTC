package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.adapter.Adapter_Food_User;
import com.example.doan.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FoodList extends AppCompatActivity{

    private List<SanPham> sanPhamList;
    private Adapter_Food_User adapter;
    private RecyclerView recyclerView;
    private SanPham sanPham;
    private TextView tv_nameCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_foodlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tv_nameCategory = findViewById(R.id.tv_nameCategory);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String category = bundle.getString("nameCategory");
        tv_nameCategory.setText(""+category+" List");
        recyclerView = findViewById(R.id.rcv_food);
        sanPhamList = new ArrayList<>();
        adapter = new Adapter_Food_User(sanPhamList);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Foods");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    sanPham = dataSnapshot.getValue(SanPham.class);
                    if (sanPham.getCategory().equals(category)) {
                        sanPhamList.add(sanPham);
                    }
                }
                Collections.reverse(sanPhamList);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodList.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });
    }


}