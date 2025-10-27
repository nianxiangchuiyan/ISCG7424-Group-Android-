package com.dinecraft.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dinecraft.app.Customer.CusFoodListActivity;
import com.dinecraft.app.Customer.CustomerBookingAddActivity;
import com.dinecraft.app.Customer.CustomerMainActivity;
import com.dinecraft.app.staff.StaffFoodListActivity;
import com.dinecraft.app.staff.StaffMainActivity;
import com.dinecraft.app.staff.StaffTableListActivity;
import com.dinecraft.app.LoginActivity;

public class BaseActivity extends AppCompatActivity {

    public void setupStaffNav(){
        Button btnBookings = findViewById(R.id.btn_staff_bookings);
        Button btnFoods = findViewById(R.id.btn_staff_foods);
        Button btnTables = findViewById(R.id.btn_staff_tables);

        if (btnBookings != null) {
            btnBookings.setOnClickListener(v -> {
                startActivity(new Intent(BaseActivity.this, StaffMainActivity.class));
            });
        }
        if(btnFoods != null) {
            btnFoods.setOnClickListener(v -> {
                startActivity(new Intent(BaseActivity.this, StaffFoodListActivity.class));
            });
        }
        if(btnTables != null) {
            btnTables.setOnClickListener(v -> {
                startActivity(new Intent(BaseActivity.this, StaffTableListActivity.class));
            });
        }

    }
    public void setupCusNav(){
        Button btnBookings = findViewById(R.id.btn_staff_bookings);
        Button btnFoods = findViewById(R.id.btn_staff_foods);
        Button btnTables = findViewById(R.id.btn_staff_tables);

        if (btnBookings != null) {
            btnBookings.setOnClickListener(v -> {
                startActivity(new Intent(BaseActivity.this, CustomerMainActivity.class));
            });
        }
        if(btnFoods != null) {
            btnFoods.setOnClickListener(v -> {
                startActivity(new Intent(BaseActivity.this, CustomerMainActivity.class));
            });
        }
        if(btnTables != null) {
            btnTables.setOnClickListener(v -> {
                startActivity(new Intent(BaseActivity.this, CustomerMainActivity.class));
            });
        }

    }
    public void setupBottomNav() {
        LinearLayout navBooking = findViewById(R.id.nav_booking);
        LinearLayout navSearch = findViewById(R.id.nav_search);
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navAccount = findViewById(R.id.nav_account);
        LinearLayout navLogin = findViewById(R.id.nav_login);

        if (navBooking != null) {
            navBooking.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    startActivity(new Intent(BaseActivity.this, CustomerBookingAddActivity.class));
                }
            });
        }
        if (navSearch != null) {
            navSearch.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //startActivity(new Intent(BaseActivity.this, SearchActivity.class));
                }
            });
        }
        if (navHome != null) {
            navHome.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    startActivity(new Intent(BaseActivity.this, MainActivity.class));
                }
            });
        }


        if (navAccount != null) {
            navAccount.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //Will need conditions for diff role
                    //startActivity(new Intent(BaseActivity.this, DashboardUserActivity.class));
                }
            });
        }


        //either show Login or Account
        if (navLogin != null) {
            navLogin.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                }
            });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
