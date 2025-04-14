package com.example.doan.activities;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.R;
import com.example.doan.model.HoaDonChiTietAdmin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonDangGiaoActivity extends AppCompatActivity {

    private TextView tvName, tvPhone, tvAddress, tvStatus;
    private Button btnMarkDone;
    private String invoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shipper_shipping);

        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        tvStatus = findViewById(R.id.tv_status);
        btnMarkDone = findViewById(R.id.btn_mark_done);

        invoiceId = getIntent().getStringExtra("invoice_id");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Detailed_Invoices");
        ref.child(invoiceId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HoaDonChiTietAdmin invoice = snapshot.getValue(HoaDonChiTietAdmin.class);
                if (invoice != null) {
                    tvName.setText("Name: " + invoice.getName());
                    tvPhone.setText("Phone: " + invoice.getPhone());
                    tvAddress.setText("Address: " + invoice.getAddress());
                    tvStatus.setText("Status: " + invoice.getStatus());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DonDangGiaoActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });


        btnMarkDone.setOnClickListener(v -> {
            ref.child(invoiceId).child("status").setValue("Done")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Đã giao hàng thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Lỗi cập nhật trạng thái!", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
