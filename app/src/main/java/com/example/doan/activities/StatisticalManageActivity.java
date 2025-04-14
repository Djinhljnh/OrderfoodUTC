package com.example.doan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.model.HoaDonChiTietAdmin;
import com.example.doan.model.SanPham;
import com.example.doan.model.TheLoai;
import com.example.doan.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticalManageActivity extends AppCompatActivity {

    private TextView tvUser;
    private TextView tvCategory;
    private TextView tvFood;
    private TextView tvFromDate, tvToDate;
    private TextView tvSumoder;
    private TextView tvSummoney;

    private List<User> userList;
    private List<TheLoai> theLoaiList;
    private List<SanPham> sanPhamList;
    private List<HoaDonChiTietAdmin> hoaDonChiTietAdminList;

    private int sum1, sum2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_manage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvUser = (TextView) findViewById(R.id.tv_user);
        tvCategory = (TextView) findViewById(R.id.tv_category);
        tvFood = (TextView) findViewById(R.id.tv_food);
        tvFromDate = (TextView) findViewById(R.id.tv_fromdate);
        tvToDate = (TextView) findViewById(R.id.tv_todate);
        tvSumoder = (TextView) findViewById(R.id.tv_sumoder);
        tvSummoney = (TextView) findViewById(R.id.tv_summoney);

        tvFromDate.setOnClickListener(v ->{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar mCalendar = Calendar.getInstance();
                    mCalendar.set(Calendar.YEAR, year);
                    mCalendar.set(Calendar.MONTH, month);
                    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String selectedDate = DateFormat.getDateInstance().format(mCalendar.getTime());
                    tvFromDate.setText(selectedDate);
                }
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE)
            );
            dialog.show();
        });

        tvToDate.setOnClickListener(v ->{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar mCalendar = Calendar.getInstance();
                    mCalendar.set(Calendar.YEAR, year);
                    mCalendar.set(Calendar.MONTH, month);
                    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String selectedDate = DateFormat.getDateInstance().format(mCalendar.getTime());
                    tvToDate.setText(selectedDate);
                }
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE)
            );
            dialog.show();
        });

        findViewById(R.id.btn_check).setOnClickListener(v ->{
            if (tvFromDate.getText().toString().equals("From date") || tvToDate.getText().toString().equals("To date")) {
                Toast.makeText(this, "Please select a date!", Toast.LENGTH_SHORT).show();
                return;
            }
            sum2 = 0;
            sum1 = 0;
            List<Date> dates = getDates(tvFromDate.getText().toString(), tvToDate.getText().toString());
            for (HoaDonChiTietAdmin hoaDonChiTietAdmin: hoaDonChiTietAdminList) {
                for(Date date:dates){
                    String date1 = DateFormat.getDateInstance().format(date);
                    if (hoaDonChiTietAdmin.getDate().toLowerCase().contains(""+date1.toLowerCase())) {
                        sum1 = sum1+1;
                        sum2 = sum2+ Integer.parseInt(hoaDonChiTietAdmin.getSum_Price());
                    }
                }
            }
            tvSumoder.setText(String.valueOf(sum1));
            tvSummoney.setText("$ "+(String.valueOf(sum2)));

        });

        userList = new ArrayList<>();
        hoaDonChiTietAdminList =  new ArrayList<>();
        theLoaiList =  new ArrayList<>();
        sanPhamList =  new ArrayList<>();
        getList();
    }

    private void getList(){
        userList.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                tvUser.setText(""+userList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticalManageActivity.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

        theLoaiList.clear();
        DatabaseReference reference1 = database.getReference("Categories");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TheLoai theLoai = dataSnapshot.getValue(TheLoai.class);
                    theLoaiList.add(theLoai);
                }
                tvCategory.setText(""+theLoaiList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticalManageActivity.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

        sanPhamList.clear();
        DatabaseReference reference2 = database.getReference("Foods");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhamList.add(sanPham);
                }
                tvFood.setText(""+sanPhamList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticalManageActivity.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

        hoaDonChiTietAdminList.clear();
        sum1 =0;
        sum2 = 0;
        DatabaseReference reference3 = database.getReference("Detailed_Invoices");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HoaDonChiTietAdmin hoaDonChiTietAdmin = dataSnapshot.getValue(HoaDonChiTietAdmin.class);
                    hoaDonChiTietAdminList.add(hoaDonChiTietAdmin);
                    sum1 = hoaDonChiTietAdminList.size();
                    sum2 = sum2+ Integer.parseInt(hoaDonChiTietAdmin.getSum_Price());
                }
                tvSumoder.setText(String.valueOf(sum1));
                tvSummoney.setText("$ "+(String.valueOf(sum2)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticalManageActivity.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private static List<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = DateFormat.getDateInstance();

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

}