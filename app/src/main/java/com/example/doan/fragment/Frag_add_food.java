package com.example.doan.fragment;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.adapter.Adapter_Food_Admin;
import com.example.doan.model.SanPham;
import com.example.doan.model.TheLoai;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Frag_add_food  extends Fragment implements Adapter_Food_Admin.Callback {
    private EditText edt_nameFood, edt_nameFoodUpdate;
    private EditText edt_priceFood;
    private EditText edt_noteFood;
    private Spinner spn_category;
    private ImageView img_food, img_foodUpdate;

    private ActivityResultLauncher<String> launcher;
    private ActivityResultLauncher<String> launcher1;
    private FirebaseStorage storage;

    private List<SanPham> sanPhamList;
    private Adapter_Food_Admin adapter;
    private RecyclerView recyclerView;
    private SanPham sanPham;
    private String image1, image2;

    private int id = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_add_food, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rcv_food);
        storage = FirebaseStorage.getInstance();
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    img_food.setImageURI(result);
                    final StorageReference reference = storage.getReference("Images_Food")
                            .child(spn_category.getSelectedItem().toString())
                            .child(edt_nameFood.getText().toString() + ".jpg");

                    reference.putFile(result)
                            .addOnSuccessListener(taskSnapshot -> {
                                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                    image1 = uri.toString();  // Cập nhật URL ảnh mới
                                    Toast.makeText(getContext(), "Tải ảnh lên thành công!", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Lỗi lấy URL ảnh", Toast.LENGTH_SHORT).show();
                                });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Tải ảnh lên thất bại", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

        launcher1 = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    img_foodUpdate.setImageURI(result);
                    final StorageReference reference = storage.getReference("Images_Food")
                            .child(spn_category.getSelectedItem().toString())
                            .child(edt_nameFoodUpdate.getText().toString() + ".jpg");

                    reference.putFile(result)
                            .addOnSuccessListener(taskSnapshot -> {
                                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                    image2 = uri.toString();  // Cập nhật URL ảnh mới
                                    Toast.makeText(getContext(), "Tải ảnh lên thành công!", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Lỗi lấy URL ảnh", Toast.LENGTH_SHORT).show();
                                });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Tải ảnh lên thất bại", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });


        view.findViewById(R.id.floatFood).setOnClickListener(v -> {
            final Dialog dialog = new Dialog(getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.setContentView(R.layout.dialog_add_food);
            edt_nameFood = dialog.findViewById(R.id.edt_nameFood);
            edt_priceFood = dialog.findViewById(R.id.edt_priceFood);
            edt_noteFood = dialog.findViewById(R.id.edt_noteFood);
            spn_category = dialog.findViewById(R.id.spn_category);
            img_food = dialog.findViewById(R.id.img_food);

            image1 = "https://firebasestorage.googleapis.com/v0/b/codedoan-2a2a1.appspot.com/o/Images_Food%2Fheo%2Fheo1.jpg?alt=media&token=9fdfb650-020c-40d5-a1b0-4c71b5129ad3";
            Picasso.get().load(image1).into(img_food);

            dialog.findViewById(R.id.btn_imgfood).setOnClickListener(v1 ->{
                launcher.launch("image/*");
            });
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Categories");
            List<TheLoai> theLoaiList = new ArrayList<>();
            ArrayAdapter<TheLoai> theLoaiArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text, theLoaiList);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        TheLoai theLoai = dataSnapshot.getValue(TheLoai.class);
                        theLoaiList.add(theLoai);
                    }
                    theLoaiArrayAdapter.notifyDataSetChanged();
                    spn_category.setAdapter(theLoaiArrayAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Get list faild!", Toast.LENGTH_LONG).show();
                }
            });


            dialog.findViewById(R.id.btn_save).setOnClickListener(view1 -> {
                String nameFood = edt_nameFood.getText().toString();
                String priceFood = edt_priceFood.getText().toString();
                String note = edt_noteFood.getText().toString();
                String category = spn_category.getSelectedItem().toString();
                id = id+1;


                if (nameFood.isEmpty()) {
                    edt_nameFood.setError("Name Category is required");
                    edt_nameFood.requestFocus();
                    return;
                }

                if (priceFood.isEmpty()) {
                    edt_priceFood.setError("Name Category is required");
                    edt_priceFood.requestFocus();
                    return;
                }

                sanPhamList.clear();
                adapter.notifyDataSetChanged();

                sanPham = new SanPham(id,category, image1, nameFood, priceFood, note);
                FirebaseDatabase.getInstance().getReference("Foods")
                        .child(String.valueOf(id))
                        .setValue(sanPham).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Add category successfully!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }
                        });
            });
            dialog.show();
        });

        sanPhamList = new ArrayList<>();
        adapter = new Adapter_Food_Admin(sanPhamList, this);
        getList();

        EditText edt_searchFood = view.findViewById(R.id.edt_searchFood);
        edt_searchFood.addTextChangedListener(new TextWatcher() {
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

    private void filter(String text) {
        ArrayList<SanPham> filteredList = new ArrayList<>();
        for (SanPham sanPham: sanPhamList){
            if (sanPham.getName_product().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(sanPham);
            }
        }
        adapter.filterList(filteredList);
    }

    private void getList(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Foods");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhamList.add(sanPham);
                }
                Collections.reverse(sanPhamList);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                id = sanPhamList.size();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void update(SanPham sanPham) {
        final Dialog dialog = new Dialog(getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_update_food);
        edt_nameFoodUpdate = dialog.findViewById(R.id.edt_nameFoodUpdate);
        EditText edt_priceFoodUpdate = dialog.findViewById(R.id.edt_priceFoodUpdate);
        EditText edt_noteFoodUpdate = dialog.findViewById(R.id.edt_noteFoodUpdate);
        Spinner spn_categoryUpdate = dialog.findViewById(R.id.spn_categoryUpdate);
        img_foodUpdate = dialog.findViewById(R.id.img_foodUpdate);

        edt_nameFoodUpdate.setText(sanPham.getName_product());
        edt_priceFoodUpdate.setText(sanPham.getPrice_product());
        edt_noteFoodUpdate.setText(sanPham.getNote_product());
        Picasso.get().load(sanPham.getImg_product()).into(img_foodUpdate);

        dialog.findViewById(R.id.btn_imgfoodUpdate).setOnClickListener(v1 ->{
            launcher1.launch("image/*");
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Categories");
        List<TheLoai> theLoaiList = new ArrayList<>();
        ArrayAdapter<TheLoai> theLoaiArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text, theLoaiList);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TheLoai theLoai = dataSnapshot.getValue(TheLoai.class);
                    theLoaiList.add(theLoai);
                }
                theLoaiArrayAdapter.notifyDataSetChanged();
                spn_categoryUpdate.setAdapter(theLoaiArrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });
        dialog.findViewById(R.id.btn_save).setOnClickListener(view1 -> {
            String nameFood = edt_nameFoodUpdate.getText().toString();
            String priceFood = edt_priceFoodUpdate.getText().toString();
            String note = edt_noteFoodUpdate.getText().toString();
            String category = spn_categoryUpdate.getSelectedItem().toString();

            if (nameFood.isEmpty()) {
                edt_nameFood.setError("Name Category is required");
                edt_nameFood.requestFocus();
                return;
            }

            if (priceFood.isEmpty()) {
                edt_priceFood.setError("Name Category is required");
                edt_priceFood.requestFocus();
                return;
            }

            sanPhamList.clear();
            adapter.notifyDataSetChanged();

            SanPham sanPham1 = new SanPham(sanPham.getId(), category, image2, nameFood, priceFood, note);
            FirebaseDatabase.getInstance().getReference("Foods")
                    .child(String.valueOf(sanPham1.getId()))
                    .setValue(sanPham1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Add category successfully!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                getList();
                            }
                        }
                    });
        });
        dialog.show();
    }
    @Override
    public void delete(SanPham sanPham) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Foods").child(String.valueOf(sanPham.getId()));

        reference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                sanPhamList.remove(sanPham);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Xóa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
