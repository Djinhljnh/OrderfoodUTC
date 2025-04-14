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
import com.example.doan.model.HoaDonChiTietAdmin;
import com.example.doan.view_holder.View_Holder_Detailed_Invoice_Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Adapter_Detailed_Invoice_Res extends RecyclerView.Adapter<View_Holder_Detailed_Invoice_Admin> {
    private List<HoaDonChiTietAdmin> hoaDonChiTietAdminList;

    public Adapter_Detailed_Invoice_Res(List<HoaDonChiTietAdmin> hoaDonChiTietAdminList) {
        this.hoaDonChiTietAdminList = hoaDonChiTietAdminList;
    }

    @NonNull
    @Override
    public View_Holder_Detailed_Invoice_Admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_detailed_invoice_admin, parent, false);

        View_Holder_Detailed_Invoice_Admin viewHolderDetailedInvoiceAdmin = new View_Holder_Detailed_Invoice_Admin(row);
        return viewHolderDetailedInvoiceAdmin;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Detailed_Invoice_Admin holder, int position) {
        HoaDonChiTietAdmin hoaDonChiTietAdmin = hoaDonChiTietAdminList.get(position);

        holder.tvDate.setText(hoaDonChiTietAdmin.getDate());
        holder.tvPrice.setText(hoaDonChiTietAdmin.getSum_Price());
        holder.line_item.setOnClickListener(v ->{
            final Dialog dialog = new Dialog(v.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.setContentView(R.layout.dialog_hodonchitiet);

            TextView tv_name = dialog.findViewById(R.id.tv_name);
            TextView tv_phone = dialog.findViewById(R.id.tv_phone);
            TextView tv_address = dialog.findViewById(R.id.tv_address);
            TextView tv_date = dialog.findViewById(R.id.tv_date);
            TextView tv_sum = dialog.findViewById(R.id.tv_sum);

            tv_name.setText("Name: "+ hoaDonChiTietAdmin.getName());
            tv_phone.setText("Phone: "+ hoaDonChiTietAdmin.getPhone());
            tv_address.setText("Address: "+ hoaDonChiTietAdmin.getAddress());
            tv_date.setText("Date: "+ hoaDonChiTietAdmin.getDate());
            tv_sum.setText("Sum: $ "+ hoaDonChiTietAdmin.getSum_Price());

            RecyclerView recyclerView = dialog.findViewById(R.id.rcv_detailed_invoice);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            List<Food_HoaDonChiTiet> food_hoaDonChiTietList = new ArrayList<>();
            Adapter_Food_HoaDonChiTiet adapter = new Adapter_Food_HoaDonChiTiet(food_hoaDonChiTietList);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Food_Detailed_Invoices");
            reference.child(String.valueOf(hoaDonChiTietAdmin.getId())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Food_HoaDonChiTiet foodHoaDonChiTiet = dataSnapshot.getValue(Food_HoaDonChiTiet.class);
                        food_hoaDonChiTietList.add(foodHoaDonChiTiet);
                        Collections.reverse(food_hoaDonChiTietList);
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


        holder.tvStatus.setText(hoaDonChiTietAdmin.getStatus());

        if (holder.tvStatus.getText().toString().equals("Confirm")) {
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackgroundResource(R.drawable.border2);
            holder.tvStatus.setOnClickListener(v -> {
                String status = "Doing";
                String uid = hoaDonChiTietAdmin.getUId();
                String date = hoaDonChiTietAdmin.getDate();
                String id = String.valueOf(hoaDonChiTietAdmin.getId());

                Log.d("DEBUG_CLICK", "tvStatus clicked!");
                Log.d("FIREBASE_UID", "UID (check null): " + (uid == null ? "null" : uid));

                FirebaseDatabase.getInstance().getReference("Detailed_Invoices")
                        .child(id)
                        .child("status")
                        .setValue(status)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(v.getContext(), "Cập nhật trạng thái trong Detailed_Invoices thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(v.getContext(), "Lỗi cập nhật trong Detailed_Invoices!", Toast.LENGTH_SHORT).show();
                            }
                        });

                DatabaseReference userInvoicesRef = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(uid)
                        .child("Detailed_Invoice");

                userInvoicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot invoiceSnap : snapshot.getChildren()) {
                            String invoiceTimestamp = invoiceSnap.child("date").getValue(String.class);
                            if (invoiceTimestamp != null && invoiceTimestamp.equals(date)) {
                                invoiceSnap.getRef().child("status").setValue(status)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(v.getContext(), "Cập nhật trạng thái thành công!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(v.getContext(), "Lỗi cập nhật Users!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FIREBASE", "Lỗi đọc dữ liệu: " + error.getMessage());
                    }
                });
            });
        }



        if (holder.tvStatus.getText().toString().equals("Doing")) {
            holder.line_item1.setBackgroundResource(R.drawable.a_doing);
            holder.tvStatus.setOnClickListener(v -> {
                String status = "finished";


                String uid = hoaDonChiTietAdmin.getUId();
                String date = hoaDonChiTietAdmin.getDate();
                String id = String.valueOf(hoaDonChiTietAdmin.getId());

                Log.d("DEBUG_CLICK", "tvStatus clicked!");
                Log.d("FIREBASE_UID", "UID (check null): " + (uid == null ? "null" : uid));

                FirebaseDatabase.getInstance().getReference("Detailed_Invoices")
                        .child(id)
                        .child("status")
                        .setValue(status)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(v.getContext(), "Cập nhật trạng thái trong Detailed_Invoices thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(v.getContext(), "Lỗi cập nhật trong Detailed_Invoices!", Toast.LENGTH_SHORT).show();
                            }
                        });

                DatabaseReference userInvoicesRef = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(uid)
                        .child("Detailed_Invoice");

                userInvoicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot invoiceSnap : snapshot.getChildren()) {
                            String invoiceTimestamp = invoiceSnap.child("date").getValue(String.class);
                            if (invoiceTimestamp != null && invoiceTimestamp.equals(date)) {
                                invoiceSnap.getRef().child("status").setValue(status)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(v.getContext(), "Cập nhật trạng thái thành công!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(v.getContext(), "Lỗi cập nhật Users!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FIREBASE", "Lỗi đọc dữ liệu: " + error.getMessage());
                    }
                });
            });
        }

        if (holder.tvStatus.getText().toString().equals("Shipping")) {
            holder.line_item1.setBackgroundResource(R.drawable.a_shipping);
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackgroundResource(R.drawable.border2);
        }
        if (holder.tvStatus.getText().toString().equals("Done")) {
            holder.line_item1.setBackgroundResource(R.drawable.a_done);
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackgroundResource(R.drawable.border2);
        }

    }

    @Override
    public int getItemCount() {
        return hoaDonChiTietAdminList == null ? 0 : hoaDonChiTietAdminList.size();
    }

    public void filterList(ArrayList<HoaDonChiTietAdmin> filteredList) {
        hoaDonChiTietAdminList = filteredList;
        notifyDataSetChanged();
    }
}
