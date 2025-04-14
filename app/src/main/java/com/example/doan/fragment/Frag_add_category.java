package com.example.doan.fragment;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.example.doan.adapter.Adapter_Category_Admin;
import com.example.doan.model.TheLoai;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Frag_add_category extends Fragment implements Adapter_Category_Admin.Callback {
    private FloatingActionButton btn_floatCategory;
    private EditText edt_nameCategory,edt_nameCategoryUpdate;
    private ImageView img_category, img_categoryUpdate;
    private ActivityResultLauncher<String> launcher;
    private ActivityResultLauncher<String> launcher1;
    private FirebaseStorage storage;

    private List<TheLoai> theLoaiList;
    private Adapter_Category_Admin adapter;
    private RecyclerView recyclerView;
    private TheLoai theLoai;
    private int id = 0;
    private String image1, image2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_add_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rcv_category);
        btn_floatCategory = view.findViewById(R.id.floatCategory);
        storage = FirebaseStorage.getInstance();
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                img_category.setImageURI(result);
                final StorageReference reference = storage.getReference("Images_Category")
                        .child(edt_nameCategory.getText().toString());
                reference.putFile(result)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Toast.makeText(getContext(),"Loading...", Toast.LENGTH_LONG).show();
                                        image1 = uri.toString();
                                    }
                                });

                            }
                        });
            }

        });
        launcher1 = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                img_categoryUpdate.setImageURI(result);
                final StorageReference reference = storage.getReference("Images_Category")
                        .child(edt_nameCategory.getText().toString());
                reference.putFile(result)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Toast.makeText(getContext(),"Loading...", Toast.LENGTH_LONG).show();
                                        image2 = uri.toString();
                                    }
                                });

                            }
                        });
            }
        });

        btn_floatCategory.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.setContentView(R.layout.dialog_add_category);
            edt_nameCategory = dialog.findViewById(R.id.edt_nameCategory);
            img_category = dialog.findViewById(R.id.img_category);
            dialog.findViewById(R.id.btn_imgcategory).setOnClickListener(v1 ->{
                launcher.launch("image/*");
            });
            image1 = "https://firebasestorage.googleapis.com/v0/b/duan-oder-doan.appspot.com/o/vdfood.png?alt=media&token=425bc41a-426c-477b-99f8-b2efa36ebc40";
            Picasso.get().load(image1).into(img_category);

            dialog.findViewById(R.id.btn_save).setOnClickListener(view1 -> {
                String nameCategory = edt_nameCategory.getText().toString();

                if (nameCategory.isEmpty()) {
                    edt_nameCategory.setError("Name Category is required");
                    edt_nameCategory.requestFocus();
                    return;
                }
                theLoaiList.clear();
                adapter.notifyDataSetChanged();
                id = id+1;
                theLoai = new TheLoai(id, image1, nameCategory);
                FirebaseDatabase.getInstance().getReference("Categories")
                        .child(String.valueOf(id))
                        .setValue(theLoai).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        theLoaiList = new ArrayList<>();
        adapter = new Adapter_Category_Admin(theLoaiList, this);
        getList();

        EditText edt_searchCategory = view.findViewById(R.id.edt_searchCategory);
        edt_searchCategory.addTextChangedListener(new TextWatcher() {
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
        ArrayList<TheLoai> filteredList = new ArrayList<>();
        for (TheLoai theLoai: theLoaiList){
            if (theLoai.getName_category().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(theLoai);
            }
        }
        adapter.filterList(filteredList);
    }

    private void getList(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Categories");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    theLoai = dataSnapshot.getValue(TheLoai.class);
                    theLoaiList.add(theLoai);
                }
                Collections.reverse(theLoaiList);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                id = theLoaiList.size();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void update(TheLoai theLoai) {
        final Dialog dialog = new Dialog(getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_update_category);
        edt_nameCategoryUpdate = dialog.findViewById(R.id.edt_nameCategoryUpdate);
        img_categoryUpdate = dialog.findViewById(R.id.img_categoryUpdate);

        edt_nameCategoryUpdate.setText(theLoai.getName_category());
        Picasso.get().load(theLoai.getImg_category()).into(img_categoryUpdate);
        dialog.findViewById(R.id.btn_imgcategoryUpdate).setOnClickListener(v1 ->{
            launcher1.launch("image/*");
        });

        dialog.findViewById(R.id.btn_save).setOnClickListener(view1 -> {
            String nameCategory = edt_nameCategoryUpdate.getText().toString();

            theLoaiList.clear();
            adapter.notifyDataSetChanged();

            if (nameCategory.isEmpty()) {
                edt_nameCategoryUpdate.setError("Name Category is required");
                edt_nameCategoryUpdate.requestFocus();
                return;
            }

            TheLoai theLoai1 = new TheLoai(theLoai.getId(), image2, nameCategory);
            FirebaseDatabase.getInstance().getReference("Categories")
                    .child(String.valueOf(theLoai1.getId()))
                    .setValue(theLoai1).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }
    @Override
    public void delete(TheLoai theLoai) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories")
                .child(String.valueOf(theLoai.getId()));

        reference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Deleted successfully!", Toast.LENGTH_SHORT).show();
                theLoaiList.remove(theLoai);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Delete failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
