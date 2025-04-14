package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.model.HoaDonChiTietAdmin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrangChuManage extends AppCompatActivity {

    private TextView tv_countOrders;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_manage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tv_countOrders = findViewById(R.id.tv_countOrders);
        tv_countOrders.setText(String.valueOf(count));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Detailed_Invoices");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HoaDonChiTietAdmin hoaDonChiTietAdmin = dataSnapshot.getValue(HoaDonChiTietAdmin.class);
                    if (hoaDonChiTietAdmin.getStatus().equals("Confirm")) {
                        count = count + 1;
                    }
                }
                tv_countOrders.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrangChuManage.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.layoutOders).setOnClickListener(view -> {
            Intent intent = new Intent(TrangChuManage.this, OdersManageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.layoutStatistical).setOnClickListener(view -> {
            Intent intent = new Intent(TrangChuManage.this, StatisticalManageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.layoutAdd).setOnClickListener(view -> {
            Intent intent = new Intent(TrangChuManage.this, AddFoodManageActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.layoutLogout).setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, DangNhap.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
    }
}