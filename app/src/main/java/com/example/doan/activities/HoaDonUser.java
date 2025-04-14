package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.model.HoaDon;
import com.example.doan.model.SanPham_Favorite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HoaDonUser extends AppCompatActivity {

    private TextView tv_nameFood, tv_noteFood, tv_priceFood, tv_quantityFood, tv_countQuantity;
    private ImageView img_food;
    private EditText edt_note;
    private List<HoaDon> hoaDonList = new ArrayList<>();
    private int number=1;
    private int sum = 0;
    private int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);

        findViewById(R.id.img_back).setOnClickListener(v ->{
            startActivity(new Intent(this, TrangChuUser.class));
        });

        findViewById(R.id.img_cart).setOnClickListener(v ->{
            startActivity(new Intent(this, GioHangUser.class));
        });

        tv_nameFood = findViewById(R.id.tv_nameFood);
        tv_noteFood = findViewById(R.id.tv_noteFood);
        tv_priceFood = findViewById(R.id.tv_priceFood);
        tv_quantityFood = findViewById(R.id.tv_quantityFood);
        tv_countQuantity = findViewById(R.id.tv_countQuantity);
        edt_note = findViewById(R.id.edt_note);
        img_food = findViewById(R.id.img_food);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("food");
        tv_nameFood.setText(bundle.getString("nameFood"));
        tv_noteFood.setText(bundle.getString("noteFood"));
        tv_priceFood.setText(bundle.getString("priceFood"));
        String imgFood = bundle.getString("imgFood");
        Picasso.get().load(imgFood).into(img_food);
        tv_quantityFood.setText(String.valueOf(number));

        findViewById(R.id.img_up).setOnClickListener(v ->{
            number = number+1;
            tv_quantityFood.setText(String.valueOf(number));
        });
        findViewById(R.id.img_down).setOnClickListener(v ->{
            if (number >1) {
                number = number-1;
                tv_quantityFood.setText(String.valueOf(number));
            }
        });
        findViewById(R.id.line_addtoCart).setOnClickListener(v ->{
            id = id+1;
            sum = 0;
            HoaDon hoaDon = new HoaDon(id, imgFood, tv_nameFood.getText().toString(), tv_priceFood.getText().toString(), tv_quantityFood.getText().toString(), edt_note.getText().toString());
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Receipt")
                    .child(String.valueOf(id))
                    .setValue(hoaDon).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(HoaDonUser.this, "Add food to cart successfully!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(HoaDonUser.this, TrangChuUser.class));
                            }
                        }
                    });

        });

        findViewById(R.id.img_favorite).setOnClickListener(v ->{
            SanPham_Favorite sanPham_favorite = new SanPham_Favorite(imgFood,tv_nameFood.getText().toString(), tv_priceFood.getText().toString(), tv_noteFood.getText().toString());
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Favorite")
                    .child(tv_nameFood.getText().toString())
                    .setValue(sanPham_favorite).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(HoaDonUser.this, "Add food to favorite successfully!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(HoaDonUser.this, TrangChuUser.class));
                            }
                        }
                    });
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Receipt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);
                    hoaDonList.add(hoaDon);
                    sum+=Integer.parseInt(hoaDon.getQuantity_Food());
                }
                id = hoaDonList.size();
                tv_countQuantity.setText(String.valueOf(sum));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HoaDonUser.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

    }

}