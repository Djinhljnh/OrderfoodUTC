package com.example.doan.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.adapter.Adapter_Receipt_User;
import com.example.doan.model.Food_HoaDonChiTiet;
import com.example.doan.model.HoaDon;
import com.example.doan.model.HoaDonChiTiet;
import com.example.doan.model.HoaDonChiTietAdmin;
import com.example.doan.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Frag_Cart extends Fragment implements Adapter_Receipt_User.Callback {
    private TextView tv_sumPrice;
    private HoaDon hoaDon;
    private List<HoaDon> hoaDonList;
    private List<HoaDonChiTietAdmin> hoaDonChiTietAdminList = new ArrayList<>();
    private Adapter_Receipt_User adapter;
    private RecyclerView recyclerView;
    private int sum=0;
    private int id =0;
    private int id1 =0;
    private int number=0;
    private String namefood = "";
    private String quantity = "";
    private String note = "";
    private List<HoaDonChiTiet> hoaDonChiTietList = new ArrayList<>();
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private EditText edt_address;
    private String name_user, email_user,pass_user, phone_user, gender_user, date_of_birth_user, image_user, address_user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rcv_cart);
        tv_sumPrice = view.findViewById(R.id.tv_sumPrice);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);

                        if (userProfile != null) {
                            name_user = userProfile.getFullName();
                            email_user = userProfile.getEmail();
                            pass_user = userProfile.getPassword();
                            phone_user = userProfile.getPhone();
                            gender_user = userProfile.getGender();
                            date_of_birth_user = userProfile.getDate_of_birth();
                            address_user = userProfile.getAddress();
                            image_user = userProfile.getImage();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(),"Something wrong happened!", Toast.LENGTH_LONG).show();
                    }
                });

        view.findViewById(R.id.img_address).setOnClickListener(v ->{
            final Dialog dialog = new Dialog(v.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.setContentView(R.layout.dialog_addess);

            edt_address = dialog.findViewById(R.id.edt_address);
            edt_address.setText(address_user);
            dialog.findViewById(R.id.btn_save).setOnClickListener(v1 ->{
                address_user = edt_address.getText().toString();
                String address = edt_address.getText().toString();
                User userProfile1 = new User(name_user, email_user, phone_user, pass_user, image_user, gender_user, date_of_birth_user, address);

                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("User")
                        .setValue(userProfile1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(),"User address updated!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }
                        });
            });
            dialog.show();
        });

        view.findViewById(R.id.line_checkout).setOnClickListener(v ->{
            if (address_user.length()<1) {
                Toast.makeText(getContext(), "Add address!", Toast.LENGTH_LONG).show();
                return;
            }
            id = id+1;
            id1 = id1+1;
            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            String defaultStatus = "Confirm";
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet(id, mydate, tv_sumPrice.getText().toString(),name_user, phone_user,address_user,defaultStatus);
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Detailed_Invoice")
                    .child(String.valueOf(id))
                    .setValue(hoaDonChiTiet).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("Receipt").removeValue();
                                hoaDonList.clear();
                                adapter.notifyDataSetChanged();
                                sum =0;
                                Toast.makeText(getContext(), "Order success!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            for (HoaDon hoaDon: hoaDonList) {
                int idfood = hoaDon.getId();
                namefood = hoaDon.getName_Food();
                quantity = hoaDon.getQuantity_Food();
                note = hoaDon.getNote_Food();
                Food_HoaDonChiTiet foodHoaDonChiTiet = new Food_HoaDonChiTiet(namefood,quantity,note);
                FirebaseDatabase.getInstance().getReference("Food_Detailed_Invoices")
                        .child(String.valueOf(id1))
                        .child(String.valueOf(idfood))
                        .setValue(foodHoaDonChiTiet).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                }
                            }
                        });
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("Food_Detailed_Invoices")
                        .child(String.valueOf(id))
                        .child(String.valueOf(idfood))
                        .setValue(foodHoaDonChiTiet).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                }
                            }
                        });
            }

            HoaDonChiTietAdmin hoaDonChiTietAdmin = new HoaDonChiTietAdmin(id1,currentUserId, mydate, tv_sumPrice.getText().toString(),name_user, phone_user,address_user,"Confirm");
            FirebaseDatabase.getInstance().getReference("Detailed_Invoices")
                    .child(String.valueOf(id1))
                    .setValue(hoaDonChiTietAdmin).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            }
                        }
                    });
        });

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference reference1 = database1.getReference("Detailed_Invoices");

        reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            HoaDonChiTietAdmin hoaDonChiTietAdmin = dataSnapshot.getValue(HoaDonChiTietAdmin.class);
                            hoaDonChiTietAdminList.add(hoaDonChiTietAdmin);
                        }
                        id1 = hoaDonChiTietAdminList.size();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Get list faild!", Toast.LENGTH_LONG).show();
                    }
                });


        hoaDonList = new ArrayList<>();
        adapter = new Adapter_Receipt_User(hoaDonList, this);
        getList();

    }

    private void getList(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Receipt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    hoaDon = dataSnapshot.getValue(HoaDon.class);
                    hoaDonList.add(hoaDon);
                    sum = sum + (Integer.parseInt(hoaDon.getPrice_Food())*Integer.parseInt(hoaDon.getQuantity_Food()));
                }
                Collections.reverse(hoaDonList);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                tv_sumPrice.setText(String.valueOf(sum));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Detailed_Invoice").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            HoaDonChiTiet hoaDonChiTiet = dataSnapshot.getValue(HoaDonChiTiet.class);
                            hoaDonChiTietList.add(hoaDonChiTiet);
                        }
                        id = hoaDonChiTietList.size();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Get list faild!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void up(HoaDon hoaDon) {
        hoaDonList.clear();
        adapter.notifyDataSetChanged();
        sum = 0;
        number = Integer.parseInt(hoaDon.getQuantity_Food());
        number = number+1;
        String quantity = String.valueOf(number);
        HoaDon hoaDon1 = new HoaDon(hoaDon.getId(),hoaDon.getImg_Food(), hoaDon.getName_Food(), hoaDon.getPrice_Food(), quantity, hoaDon.getNote_Food());
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Receipt")
                .child(String.valueOf(hoaDon.getId()))
                .setValue(hoaDon1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
    }

    @Override
    public void down(HoaDon hoaDon) {
        hoaDonList.clear();
        adapter.notifyDataSetChanged();
        sum = 0;
        tv_sumPrice.setText(String.valueOf(sum));
        number = Integer.parseInt(hoaDon.getQuantity_Food());
        if (number >1) {
            number = number-1;
            String quantity = String.valueOf(number);

            HoaDon hoaDon1 = new HoaDon(hoaDon.getId(), hoaDon.getImg_Food(), hoaDon.getName_Food(), hoaDon.getPrice_Food(), quantity, hoaDon.getNote_Food());
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Receipt")
                    .child(String.valueOf(hoaDon.getId()))
                    .setValue(hoaDon1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            }
                        }
                    });

        }
    }

}
