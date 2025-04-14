package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DangNhap extends AppCompatActivity {
    private EditText edt_email;
    private EditText edt_pass;
    private TextView tv_forgetpass;
    private TextView tv_signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        tv_forgetpass = findViewById(R.id.tv_forgetpass);
        tv_signup = findViewById(R.id.tv_signup);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btn_login).setOnClickListener(v ->{
            login();
        });

        tv_forgetpass.setOnClickListener(v ->{
            Intent intent = new Intent(this, XacNhanPass_Email.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

        tv_signup.setOnClickListener(v ->{
            Intent intent = new Intent(this, DangKy.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
    }

    private void login(){
        String email = edt_email.getText().toString();
        String password = edt_pass.getText().toString();

        if (email.isEmpty()) {
            edt_email.setError("E-mail is required!");
            edt_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_email.setError("Please enter a valid email!");
            edt_email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edt_pass.setError("Password is required!");
            edt_pass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edt_pass.setError("Min password length should be 6 characters!");
            edt_pass.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if (edt_email.getText().toString().equalsIgnoreCase("admin@gmail.com") && edt_pass.getText().toString().equalsIgnoreCase("123456") ) {
                                Intent intent = new Intent(DangNhap.this, TrangChuManage.class);
                                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(DangNhap.this).toBundle();
                                startActivity(intent, bundle);
                            } else if (edt_email.getText().toString().equalsIgnoreCase("res@gmail.com") && edt_pass.getText().toString().equalsIgnoreCase("123456") ) {
                                Intent intent = new Intent(DangNhap.this, TrangChuRestaurant.class);
                                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(DangNhap.this).toBundle();
                                startActivity(intent, bundle);
                            } else if (edt_email.getText().toString().equalsIgnoreCase("ship@gmail.com") && edt_pass.getText().toString().equalsIgnoreCase("123456") ) {
                                Intent intent = new Intent(DangNhap.this, TrangChuShipper.class);
                                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(DangNhap.this).toBundle();
                                startActivity(intent, bundle);
                            }else {
                                Toast.makeText(DangNhap.this, "User has been login successfully!",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(DangNhap.this, TrangChuUser.class);
                                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(DangNhap.this).toBundle();
                                startActivity(intent, bundle);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(DangNhap.this, "Failed to login! Please check your credentials!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
            });
        }


}