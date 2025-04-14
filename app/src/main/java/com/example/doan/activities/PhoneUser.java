package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PhoneUser extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private EditText edt_phone;
    private String name, email,pass, gender, date_of_birth, image, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_user);

        edt_phone = findViewById(R.id.edt_phone);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String phone = userProfile.getPhone();
                    edt_phone.setText(""+phone);
                    email = userProfile.getEmail();
                    pass = userProfile.getPassword();
                    name = userProfile.getFullName();
                    gender = userProfile.getGender();
                    date_of_birth = userProfile.getDate_of_birth();
                    image = userProfile.getImage();
                    address = userProfile.getAddress();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PhoneUser.this,"Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(v ->{
            String phone = edt_phone.getText().toString();
            User userProfile1 = new User(name, email, phone,pass, image, gender, date_of_birth, address);

            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("User")
                    .setValue(userProfile1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PhoneUser.this,"User phone no updated!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(PhoneUser.this, GioiThieuUser.class);
                                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(PhoneUser.this).toBundle();
                                startActivity(intent, bundle);
                            }
                        }
                    });
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_phone);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}