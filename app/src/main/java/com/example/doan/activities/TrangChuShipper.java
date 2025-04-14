package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.adapter.Adapter_Detailed_Invoice_Res;
import com.example.doan.adapter.Adapter_Detailed_Invoice_Shiper;
import com.example.doan.model.HoaDonChiTietAdmin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrangChuShipper extends AppCompatActivity {

    private HoaDonChiTietAdmin hoaDonChiTietAdmin;
    private List<HoaDonChiTietAdmin> hoaDonChiTietAdminList;
    private Adapter_Detailed_Invoice_Shiper adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_ship);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        findViewById(R.id.btn_logout).setOnClickListener(v ->{
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, DangNhap.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

        recyclerView = findViewById(R.id.rcv_oder);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        hoaDonChiTietAdminList = new ArrayList<>();
        adapter = new Adapter_Detailed_Invoice_Shiper(hoaDonChiTietAdminList);
        getList();

        EditText edt_search = findViewById(R.id.edt_search);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void getList(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Detailed_Invoices");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hoaDonChiTietAdminList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    hoaDonChiTietAdmin = dataSnapshot.getValue(HoaDonChiTietAdmin.class);
                    hoaDonChiTietAdminList.add(hoaDonChiTietAdmin);
                    /*if (hoaDonChiTietAdmin != null) {
                        String status = hoaDonChiTietAdmin.getStatus();
                        if ("Finished".equals(status) || "Shipping".equals(status) || "Done".equals(status)) {
                            hoaDonChiTietAdminList.add(hoaDonChiTietAdmin);
                        }
                    }*/
                }
                Collections.reverse(hoaDonChiTietAdminList);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrangChuShipper.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filter(String text) {
        ArrayList<HoaDonChiTietAdmin> filteredList = new ArrayList<>();
        for (HoaDonChiTietAdmin hoaDon: hoaDonChiTietAdminList){
            if (hoaDon.getDate().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(hoaDon);
            }
        }
        adapter.filterList(filteredList);
    }
}