package com.example.doan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.doan.R;

public class ThongBao extends AppCompatActivity {
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_advertise);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView img_advertise = findViewById(R.id.img_advertise);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        int img = bundle.getInt("image");
        if (img == R.drawable.qc1) {
            uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/duan-oder-doan.appspot.com/o/Images_Advertisement%2Fqc1.jpg?alt=media&token=5b30e500-25de-4c8c-a265-15512ecfb751");
        }
        if (img == R.drawable.vdfood) {
            uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/codedoan-2a2a1.appspot.com/o/Images_Food%2Fheo%2Fvdfood.png?alt=media&token=da283d45-2929-4c78-99fd-8d611afbf083");
        }
        if (img == R.drawable.qc3) {
            uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/duan-oder-doan.appspot.com/o/Images_Advertisement%2Fqc3.png?alt=media&token=acbe166b-03de-4b96-955b-6edc1c16ad89");
        }
        img_advertise.setBackgroundResource(img);

        findViewById(R.id.btn_share).setOnClickListener(v ->{
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, null));
        });


    }
}