package com.example.doan.adapter;

import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.model.Food_HoaDonChiTiet;
import com.example.doan.model.HoaDonChiTiet;
import com.example.doan.view_holder.View_Holder_Detailed_Invoice_User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Adapter_Detailed_Invoice_User extends RecyclerView.Adapter<View_Holder_Detailed_Invoice_User> {
    private List<HoaDonChiTiet> hoaDonChiTietList;

    public Adapter_Detailed_Invoice_User(List<HoaDonChiTiet> hoaDonChiTietList) {
        this.hoaDonChiTietList = hoaDonChiTietList;
    }

    @NonNull
    @Override
    public View_Holder_Detailed_Invoice_User onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_detailed_invoice_user, parent, false);

        View_Holder_Detailed_Invoice_User viewHolderDetailedInvoiceUser = new View_Holder_Detailed_Invoice_User(row);
        return viewHolderDetailedInvoiceUser;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Detailed_Invoice_User holder, int position) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietList.get(position);
        
        holder.tvDate.setText(hoaDonChiTiet.getDate());
        holder.tvPrice.setText(hoaDonChiTiet.getSum_Price());
        holder.line_item.setOnClickListener(v ->{
            final Dialog dialog = new Dialog(v.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.setContentView(R.layout.dialog_hodonchitiet);

            TextView tv_name = dialog.findViewById(R.id.tv_name);
            TextView tv_phone = dialog.findViewById(R.id.tv_phone);
            TextView tv_address = dialog.findViewById(R.id.tv_address);
            TextView tv_date = dialog.findViewById(R.id.tv_date);
            TextView tv_status = dialog.findViewById(R.id.tv_status);
            TextView tv_sum = dialog.findViewById(R.id.tv_sum);


            tv_name.setText("Name: "+ hoaDonChiTiet.getName());
            tv_phone.setText("Phone: "+ hoaDonChiTiet.getPhone());
            tv_address.setText("Address: "+ hoaDonChiTiet.getAddress());
            tv_date.setText("Date: "+ hoaDonChiTiet.getDate());
            tv_sum.setText("Sum: $ "+ hoaDonChiTiet.getSum_Price());

            RecyclerView recyclerView = dialog.findViewById(R.id.rcv_detailed_invoice);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            List<Food_HoaDonChiTiet> food_hoaDonChiTietList = new ArrayList<>();
            Adapter_Food_HoaDonChiTiet adapter = new Adapter_Food_HoaDonChiTiet(food_hoaDonChiTietList);
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference reference1 = database.getReference("Users");
            reference1.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .child("Detailed_Invoice")
                    .child(String.valueOf(hoaDonChiTiet.getId())).addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.d("FirebaseData", "Invoice found: " + snapshot.getValue().toString());
                        HoaDonChiTiet invoice = snapshot.getValue(HoaDonChiTiet.class);
                        if (invoice != null && invoice.getStatus() != null) {
                            tv_status.setText("Status: " + invoice.getStatus());
                        } else {
                            tv_status.setText("Status: Not Available");
                        }
                    } else {
                        Log.d("FirebaseData", "Invoice not found for ID: " + hoaDonChiTiet.getId());
                        tv_status.setText("Status: Not Found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Database error: " + error.getMessage());
                    Toast.makeText(v.getContext(), "Failed to get status!", Toast.LENGTH_SHORT).show();
                }
            });





            DatabaseReference reference = database.getReference("Users");
            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Food_Detailed_Invoices")
                    .child(String.valueOf(hoaDonChiTiet.getId())).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Food_HoaDonChiTiet foodHoaDonChiTiet = dataSnapshot.getValue(Food_HoaDonChiTiet.class);
                                food_hoaDonChiTietList.add(foodHoaDonChiTiet);
                            }
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(v.getContext(), "Get list faild!", Toast.LENGTH_LONG).show();
                        }
                    });

            dialog.show();

        });
    }

    @Override
    public int getItemCount() {
        return hoaDonChiTietList == null ? 0 : hoaDonChiTietList.size();
    }
}
