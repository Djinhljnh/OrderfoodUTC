package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class GioiThieuUser extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_email;
    private CircleImageView img_avatar;
    private TextView tv_gender;
    private TextView tv_dateofbirth;

    private String name, email, pass, phone, gender, date_of_birth, image, address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioi_thieu_user);

        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_email = findViewById(R.id.tv_email);
        img_avatar = findViewById(R.id.img_avatar);
        tv_gender = findViewById(R.id.tv_gender);
        tv_dateofbirth = findViewById(R.id.tv_dateofbirth);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    name = userProfile.getFullName();
                    tv_name.setText(""+name);
                    image = userProfile.getImage();
                    Picasso.get().load(image).into(img_avatar);
                    email = userProfile.getEmail();
                    tv_email.setText(""+email);
                    pass = userProfile.getPassword();
                    phone = userProfile.getPhone();
                    tv_phone.setText(""+phone);
                    gender = userProfile.getGender();
                    tv_gender.setText(""+gender);
                    date_of_birth = userProfile.getDate_of_birth();
                    address = userProfile.getAddress();
                    tv_dateofbirth.setText(""+date_of_birth);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GioiThieuUser.this,"Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_introduce);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        findViewById(R.id.ln_avatar).setOnClickListener(v ->{
            Intent intent = new Intent(this, AvatarUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

        findViewById(R.id.ln_name).setOnClickListener(v ->{
            Intent intent = new Intent(this, NameUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

        findViewById(R.id.ln_email).setOnClickListener(v ->{
            Intent intent = new Intent(this, EmailUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

        findViewById(R.id.ln_phone).setOnClickListener(v ->{
            Intent intent = new Intent(this, PhoneUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

        findViewById(R.id.ln_gender).setOnClickListener(v ->{
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_bottom_gioi_tinh);
            dialog.findViewById(R.id.tv_none).setOnClickListener(view ->{
                tv_gender.setText("");
                update_gender();
                dialog.dismiss();
            });
            dialog.findViewById(R.id.tv_male).setOnClickListener(view ->{
                tv_gender.setText("Male");
                update_gender();
                dialog.dismiss();
            });
            dialog.findViewById(R.id.tv_female).setOnClickListener(view ->{
                tv_gender.setText("Female");
                update_gender();
                dialog.dismiss();
            });
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);

        });


        findViewById(R.id.ln_dateofbirth).setOnClickListener(v ->{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    int nam = i;
                    int thang = i1;
                    int ngay = i2;

                    tv_dateofbirth.setText(ngay + "/" + (thang + 1) + "/" + nam);
                    update_date();
                }
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE)
            );
            dialog.show();
        });



    }

    private void update_gender(){
        User userProfile1 = new User(name, email, phone, pass, image, tv_gender.getText().toString(), tv_dateofbirth.getText().toString(), address);

        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("User")
                .setValue(userProfile1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(GioiThieuUser.this,"User gender no updated!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void update_date(){
        User userProfile1 = new User(name, email, phone,pass, image, tv_gender.getText().toString(), tv_dateofbirth.getText().toString(), address);

        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("User")
                .setValue(userProfile1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(GioiThieuUser.this,"User date of birth no updated!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}