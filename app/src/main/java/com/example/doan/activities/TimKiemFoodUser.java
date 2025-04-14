package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import java.util.List;

public class TimKiemFoodUser extends AppCompatActivity {

    private List<SanPham> sanPhamList;
    private Adapter_Food_User adapter;
    private RecyclerView recyclerView;
    private SanPham sanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem_food_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.rcv_searchlist);
        recyclerView.setVisibility(View.INVISIBLE);

        sanPhamList = new ArrayList<>();
        adapter = new Adapter_Food_User(sanPhamList);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Foods");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhamList.add(sanPham);
                }
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TimKiemFoodUser.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

        EditText edt_searchFood = findViewById(R.id.edt_searchfood);

        edt_searchFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                recyclerView.setVisibility(View.VISIBLE);
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<SanPham> filteredList = new ArrayList<>();
        for (SanPham sanPham: sanPhamList){
            if (sanPham.getName_product().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(sanPham);
            }
        }
        adapter.filterList(filteredList);
    }

}