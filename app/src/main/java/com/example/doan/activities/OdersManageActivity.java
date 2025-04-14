package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.adapter.Adapter_Detailed_Invoice_Admin;
import com.example.doan.model.HoaDonChiTietAdmin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OdersManageActivity extends AppCompatActivity {

    private HoaDonChiTietAdmin hoaDonChiTietAdmin;
    private List<HoaDonChiTietAdmin> hoaDonChiTietAdminList;
    private Adapter_Detailed_Invoice_Admin adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oders_manage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.rcv_oder);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        hoaDonChiTietAdminList = new ArrayList<>();
        adapter = new Adapter_Detailed_Invoice_Admin(hoaDonChiTietAdminList);
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
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            hoaDonChiTietAdmin = dataSnapshot.getValue(HoaDonChiTietAdmin.class);
                            hoaDonChiTietAdminList.add(hoaDonChiTietAdmin);
                        }
                        Collections.reverse(hoaDonChiTietAdminList);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OdersManageActivity.this, "Get list faild!", Toast.LENGTH_LONG).show();
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