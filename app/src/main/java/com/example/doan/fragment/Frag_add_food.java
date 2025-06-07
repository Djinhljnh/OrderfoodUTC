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

public class Frag_add_food extends Fragment implements Adapter_Food_Admin.Callback {

    private EditText edt_nameFood, edt_nameFoodUpdate;
    private EditText edt_priceFood, edt_noteFood;
    private Spinner spn_category;
    private ImageView img_food, img_foodUpdate;
    private RecyclerView recyclerView;

    private ActivityResultLauncher<String> launcher, launcher1;
    private FirebaseStorage storage;
    private List<SanPham> sanPhamList;
    private Adapter_Food_Admin adapter;
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
        sanPhamList = new ArrayList<>();
        adapter = new Adapter_Food_Admin(sanPhamList, this);
        recyclerView.setAdapter(adapter);
        getList();

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                img_food.setImageURI(result);
                uploadImage(result, true);
            }
        });

        launcher1 = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                img_foodUpdate.setImageURI(result);
                uploadImage(result, false);
            }
        });

        view.findViewById(R.id.floatFood).setOnClickListener(v -> showAddDialog());

        EditText edt_searchFood = view.findViewById(R.id.edt_searchFood);
        edt_searchFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void uploadImage(Uri result, boolean isAddMode) {
        String name = isAddMode ? edt_nameFood.getText().toString() : edt_nameFoodUpdate.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Tên món không được để trống trước khi chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = spn_category != null && spn_category.getSelectedItem() != null ? spn_category.getSelectedItem().toString() : "unknown";

        final StorageReference reference = storage.getReference("Images_Food")
                .child(category)
                .child(name + ".jpg");

        reference.putFile(result)
                .addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                    if (isAddMode) image1 = uri.toString();
                    else image2 = uri.toString();
                    Toast.makeText(getContext(), "Tải ảnh lên thành công!", Toast.LENGTH_SHORT).show();
                }))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Tải ảnh lên thất bại", Toast.LENGTH_SHORT).show());
    }

    private void showAddDialog() {
        Dialog dialog = new Dialog(getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_add_food);

        edt_nameFood = dialog.findViewById(R.id.edt_nameFood);
        edt_priceFood = dialog.findViewById(R.id.edt_priceFood);
        edt_noteFood = dialog.findViewById(R.id.edt_noteFood);
        spn_category = dialog.findViewById(R.id.spn_category);
        img_food = dialog.findViewById(R.id.img_food);

        image1 = "";

        dialog.findViewById(R.id.btn_imgfood).setOnClickListener(v -> launcher.launch("image/*"));
        setupSpinner(spn_category);

        dialog.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String name = edt_nameFood.getText().toString();
            String price = edt_priceFood.getText().toString();
            String note = edt_noteFood.getText().toString();
            String category = spn_category.getSelectedItem().toString();

            if (name.isEmpty() || price.isEmpty()) {
                Toast.makeText(getContext(), "Tên và giá món không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            id++;
            SanPham sanPham = new SanPham(id, category, image1, name, price, note);
            FirebaseDatabase.getInstance().getReference("Foods").child(String.valueOf(id)).setValue(sanPham)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Thêm món thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            getList();
                        }
                    });
        });

        dialog.show();
    }

    private void setupSpinner(Spinner spinner) {
        List<TheLoai> theLoaiList = new ArrayList<>();
        ArrayAdapter<TheLoai> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text, theLoaiList);
        spinner.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference("Categories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        theLoaiList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            TheLoai theLoai = ds.getValue(TheLoai.class);
                            if (theLoai != null) theLoaiList.add(theLoai);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Không tải được thể loại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filter(String text) {
        List<SanPham> filteredList = new ArrayList<>();
        for (SanPham sp : sanPhamList) {
            if (sp.getName_product().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(sp);
            }
        }
        adapter.filterList((ArrayList<SanPham>) filteredList);
    }

    private void getList() {
        FirebaseDatabase.getInstance().getReference("Foods")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sanPhamList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SanPham sp = ds.getValue(SanPham.class);
                            if (sp != null) sanPhamList.add(sp);
                        }
                        Collections.reverse(sanPhamList);
                        adapter.notifyDataSetChanged();
                        id = sanPhamList.size();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Không lấy được danh sách món", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void update(SanPham sanPham) {
        Dialog dialog = new Dialog(getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_update_food);

        edt_nameFoodUpdate = dialog.findViewById(R.id.edt_nameFoodUpdate);
        EditText edt_priceUpdate = dialog.findViewById(R.id.edt_priceFoodUpdate);
        EditText edt_noteUpdate = dialog.findViewById(R.id.edt_noteFoodUpdate);
        Spinner spn_categoryUpdate = dialog.findViewById(R.id.spn_categoryUpdate);
        img_foodUpdate = dialog.findViewById(R.id.img_foodUpdate);

        edt_nameFoodUpdate.setText(sanPham.getName_product());
        edt_priceUpdate.setText(sanPham.getPrice_product());
        edt_noteUpdate.setText(sanPham.getNote_product());
        Picasso.get().load(sanPham.getImg_product()).into(img_foodUpdate);

        image2 = sanPham.getImg_product();

        dialog.findViewById(R.id.btn_imgfoodUpdate).setOnClickListener(v -> launcher1.launch("image/*"));
        setupSpinner(spn_categoryUpdate);

        dialog.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String name = edt_nameFoodUpdate.getText().toString();
            String price = edt_priceUpdate.getText().toString();
            String note = edt_noteUpdate.getText().toString();
            String category = spn_categoryUpdate.getSelectedItem().toString();

            if (name.isEmpty() || price.isEmpty()) {
                Toast.makeText(getContext(), "Tên và giá món không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            SanPham updated = new SanPham(sanPham.getId(), category, image2, name, price, note);
            FirebaseDatabase.getInstance().getReference("Foods")
                    .child(String.valueOf(updated.getId())).setValue(updated)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Cập nhật món thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            getList();
                        }
                    });
        });

        dialog.show();
    }

    @Override
    public void delete(SanPham sanPham) {
        FirebaseDatabase.getInstance().getReference("Foods")
                .child(String.valueOf(sanPham.getId())).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Xoá món thành công", Toast.LENGTH_SHORT).show();
                        getList();
                    }
                });
    }
}