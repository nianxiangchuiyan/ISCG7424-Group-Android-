package com.dinecraft.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dinecraft.app.Customer.CusFoodListActivity;
import com.dinecraft.app.Customer.CustomerBookingAddActivity;
import com.dinecraft.app.Customer.CustomerMainActivity;
import com.dinecraft.app.staff.StaffFoodListActivity;
import com.dinecraft.app.staff.StaffMainActivity;
import com.dinecraft.app.staff.StaffTableListActivity;
import com.dinecraft.app.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    public void setupStaffNav() {
        Button btnBookings = findViewById(R.id.btn_staff_bookings);
        Button btnFoods = findViewById(R.id.btn_staff_foods);
        Button btnTables = findViewById(R.id.btn_staff_tables);

        if (btnBookings != null) {
            btnBookings.setOnClickListener(v -> {
                startActivity(new Intent(BaseActivity.this, StaffMainActivity.class));
            });
        }
        if (btnFoods != null) {
            btnFoods.setOnClickListener(v -> {
                startActivity(new Intent(BaseActivity.this, StaffFoodListActivity.class));
            });
        }
        if (btnTables != null) {
            btnTables.setOnClickListener(v -> {
                startActivity(new Intent(BaseActivity.this, StaffTableListActivity.class));
            });
        }

    }

    public void setupCusNav() {
        Button btnBookings = findViewById(R.id.btn_staff_bookings);
        Button btnFoods = findViewById(R.id.btn_staff_foods);
        Button btnTables = findViewById(R.id.btn_staff_tables);

        if (btnBookings != null) {
            btnBookings.setOnClickListener(v -> {
                startActivity(new Intent(BaseActivity.this, CustomerMainActivity.class));
            });
        }
        if (btnFoods != null) {
            btnFoods.setOnClickListener(v -> {
                startActivity(new Intent(BaseActivity.this, CustomerMainActivity.class));
            });
        }
        if (btnTables != null) {
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
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BaseActivity.this, CustomerBookingAddActivity.class));
                }
            });
        }
        if (navSearch != null) {
            navSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startActivity(new Intent(BaseActivity.this, SearchActivity.class));
                }
            });
        }
        if (navHome != null) {
            navHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BaseActivity.this, MainActivity.class));
                }
            });
        }

        if(currentUser != null) {

            if (navAccount != null) {
                navLogin.setVisibility(View.GONE);
                navAccount.setVisibility(View.VISIBLE);
                navAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Will need conditions for diff role
                        startActivity(new Intent(BaseActivity.this, ProfileEditActivity.class));
                    }
                });
            }

        }else {

            //either show Login or Account
            if (navLogin != null) {
                navLogin.setVisibility(View.VISIBLE);
                navAccount.setVisibility(View.GONE);
                navLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                    }
                });
            }
        }
    }

    public void setupTopProfile() {
        TextView tv_email = findViewById(R.id.tv_staff_email);
        ImageView iv_logout = findViewById(R.id.iv_staff_logout);
        if(tv_email==null || iv_logout==null) return;

        tv_email.setText(currentUser.getDisplayName() + "\n" + currentUser.getEmail());
        iv_logout.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog1, which) -> {
                        mAuth.signOut();
                        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("No", (dialog1, which) -> {
                        dialog1.dismiss();
                    })
                    .show();
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Verify user is logged in, otherwise redir to login activity.
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(BaseActivity.this, LoginActivity.class));
            finish();
        }
    }
}
