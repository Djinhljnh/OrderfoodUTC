package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.adapter.Adapter_Category_User;
import com.example.doan.adapter.Adapter_Food_Popular_User;
import com.example.doan.adapter.PagerAdapter_ThongBao;
import com.example.doan.model.SanPham_Popular;
import com.example.doan.model.TheLoai;
import com.example.doan.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;


public class TrangChuUser extends AppCompatActivity implements Adapter_Category_User.Callback {

    private ViewPager2 viewPager2;
    int[] images = {R.drawable.qc1,R.drawable.qc2,R.drawable.qc3,R.drawable.qc4,R.drawable.vdfood};
    private PagerAdapter_ThongBao pagerAdapterThongBao;
    private CircleIndicator3 circleIndicator;
    private Timer timer;
    private EditText edt_searchfood;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private CircleImageView img_avatar;
    private TextView tv_name;

    private List<SanPham_Popular> sanPham_popularList = new ArrayList<>();
    private Adapter_Food_Popular_User adapter_food_popular_user = new Adapter_Food_Popular_User(sanPham_popularList);
    private List<TheLoai> theLoaiList = new ArrayList<>();
    private Adapter_Category_User adapter_category = new Adapter_Category_User(theLoaiList, this);
    private RecyclerView rcv_category, rcv_popular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_user);

        tv_name = findViewById(R.id.tv_name);
        img_avatar = findViewById(R.id.img_avatar);
        rcv_category = findViewById(R.id.rcv_category);
        rcv_popular = findViewById(R.id.rcv_popular);
        getList();

        // Lay thong tin tu firebase hien thi len
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String fullName = userProfile.getFullName();
                    tv_name.setText("Hi "+ fullName+"!");
                    String image1 = userProfile.getImage();
                    Picasso.get().load(image1).into(img_avatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrangChuUser.this,"Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });



        edt_searchfood = findViewById(R.id.edt_searchfood);
        edt_searchfood.setFocusable(false);
        edt_searchfood.setOnClickListener(v->{
            Intent intent = new Intent(this, TimKiemFoodUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
        findViewById(R.id.img_avatar).setOnClickListener(v ->{
            Intent intent = new Intent(this, GioiThieuUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

        viewPager2 = findViewById(R.id.viewpager2_thongbao);
        pagerAdapterThongBao = new PagerAdapter_ThongBao(images);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager2.setAdapter(pagerAdapterThongBao);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(8));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = 1-Math.abs(position);

                page.setScaleY(0.8f+v*0.2f);
            }
        });

        circleIndicator = findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager2);
        pagerAdapterThongBao.registerAdapterDataObserver(circleIndicator.getAdapterDataObserver());
        circleIndicator.createIndicators(5,0);
        circleIndicator.animatePageSelected(2);

        viewPager2.setPageTransformer(transformer);
        autoSile();


        findViewById(R.id.img_trangchu).setOnClickListener(v ->{
            Intent intent = new Intent(this, TrangChuUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
        findViewById(R.id.img_tintuc).setOnClickListener(v ->{
            Intent intent = new Intent(this, YeuThichUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
        findViewById(R.id.img_giohang).setOnClickListener(v ->{
            Intent intent = new Intent(this, GioHangUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
        findViewById(R.id.img_thongtin).setOnClickListener(v ->{
            Intent intent = new Intent(this, ThongTinUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

    }

    private void autoSile() {
        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager2.getCurrentItem();
                        int totalItem = 5;
                        if (currentItem <totalItem) {
                            currentItem++;
                            viewPager2.setCurrentItem(currentItem);

                        }
                        if (currentItem >= 5) {
                            viewPager2.setCurrentItem(0);
                        }
                    }
                });
            }
        },6000, 6000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void getList(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Categories");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TheLoai theLoai = dataSnapshot.getValue(TheLoai.class);
                    theLoaiList.add(theLoai);
                }
                adapter_category.notifyDataSetChanged();
                rcv_category.setAdapter(adapter_category);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrangChuUser.this, "Get list category faild!", Toast.LENGTH_LONG).show();
            }
        });
        DatabaseReference reference1 = database.getReference("FoodPopulars");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SanPham_Popular sanPham_popular = dataSnapshot.getValue(SanPham_Popular.class);
                    sanPham_popularList.add(sanPham_popular);
                }
                Collections.reverse(sanPham_popularList);
                adapter_food_popular_user.notifyDataSetChanged();
                rcv_popular.setAdapter(adapter_food_popular_user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrangChuUser.this, "Get list category faild!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void open(TheLoai theLoai) {
        Intent intent = new Intent(this, FoodList.class);
        Bundle bundle = new Bundle();
        bundle.putString("nameCategory", theLoai.getName_category());
        intent.putExtra("bundle", bundle);
        startActivity(intent, bundle);
    }
}