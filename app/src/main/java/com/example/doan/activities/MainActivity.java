package com.example.doan.activities;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, TrangChuUser.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img_mhc1 = findViewById(R.id.img_mhc1);
        TextView tv_mhc = findViewById(R.id.tv_mhc);
        ImageView img_mhc2 = findViewById(R.id.img_mhc2);
        ImageView img_logo1 = findViewById(R.id.img_logo1);
        ImageView img_logo2 = findViewById(R.id.img_logo2);
        LinearLayout layout_mhc2 = findViewById(R.id.layout_mhc2);
        TextView tv_mhc1 = findViewById(R.id.tv_mhc1);
        TextView tv_mhc2 = findViewById(R.id.tv_mhc2);
        LinearLayout layout_mhc3 = findViewById(R.id.layout_mhc3);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.animation1);
        tv_mhc.startAnimation(animation);

        Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.animation2);
        img_mhc2.startAnimation(animation2);

        Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.animation);
        img_logo1.startAnimation(animation1);

        Animation animation3 = AnimationUtils.loadAnimation(this,R.anim.animation3);
        img_mhc1.startAnimation(animation3);

        Animation animation4 = AnimationUtils.loadAnimation(this,R.anim.animation4);
        layout_mhc2.startAnimation(animation4);

        Animation animation5 = AnimationUtils.loadAnimation(this,R.anim.animation5);
        tv_mhc1.startAnimation(animation5);
        tv_mhc2.startAnimation(animation5);
        img_logo2.startAnimation(animation5);

        Animation animation6 = AnimationUtils.loadAnimation(this,R.anim.animation6);
        layout_mhc3.startAnimation(animation6);

        img_mhc1.setVisibility(View.INVISIBLE);
        img_mhc2.setVisibility(View.INVISIBLE);
        img_logo1.setVisibility(View.INVISIBLE);
        tv_mhc.setVisibility(View.INVISIBLE);

        findViewById(R.id.btn_login).setOnClickListener(v ->{
            Intent intent = new Intent(this, DangNhap.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

        findViewById(R.id.btn_signup).setOnClickListener(v ->{
            Intent intent = new Intent(this, DangKy.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

    }
}