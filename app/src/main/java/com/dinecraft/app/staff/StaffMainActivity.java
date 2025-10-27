package com.dinecraft.app.staff;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.Booking;
import com.dinecraft.app.Config;
import com.dinecraft.app.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StaffMainActivity extends BaseActivity {

    private FirebaseFirestore db;
    private List<Booking> allBookings;
    private RecyclerView recyclerView;
    private Spinner spn_table, spn_timeslot;
    private TextView tv_date;
    private ImageView iv_datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout first
        setContentView(R.layout.activity_staff_main);

        // Set window insets for the UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the bottom navigation bar
        setupBottomNav();
        setupStaffNav();

        spn_table = findViewById(R.id.spn_table);
        spn_timeslot = findViewById(R.id.spn_timeslot);
        tv_date = findViewById(R.id.tv_staff_date);
        iv_datepicker = findViewById(R.id.iv_staff_datepicker);

        // Initialize the spinner for timeslots
        Config.init_spinner(spn_timeslot, R.array.staff_timeslot, this);
        // Initialize the spinner for tables
        Config.getInstance().init_table_spinner(spn_table, this);
        iv_datepicker.setOnClickListener(v-> showDatePicker(tv_date));

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        db.collection("bookings")
                //.whereEqualTo("staff_id", Config.getInstance().getStaffId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allBookings = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Booking booking = document.toObject(Booking.class);
                        allBookings.add(booking);
                    }
                    refreshBookingList(allBookings);

                })
                .addOnFailureListener( e -> {
                    Log.e("FirestoreTest", "Failed to load bookings", e);
                    Toast.makeText(this, "Failed to load bookings", Toast.LENGTH_SHORT).show();
                });


    }

    private void showDatePicker(TextView tvDate) {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 创建日期选择器对话框
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (DatePicker view, int year1, int month1, int dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    tvDate.setText(selectedDate);
                },
                year, month, day
        );
        dialog.show();
    }

    public void refreshBookingList(List<Booking> listToShow){
        if(listToShow==null){
            Toast.makeText(this, "No bookings to show", Toast.LENGTH_SHORT).show();
            return;
        }
        recyclerView = findViewById(R.id.rv_staff_booking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookingRVAdapter adapter = new BookingRVAdapter(listToShow, this);
        recyclerView.setAdapter(adapter);

    }

//    // Load data for Food from Firestore
//    public void loadFoodData(View view) {
//        List<FoodModel> foodList = new ArrayList<>();
//        FoodAdapter adapter = new FoodAdapter(foodList);
//        recyclerView.setAdapter(adapter);
//
//        db.collection("Food")
//                .get()
//                .addOnSuccessListener(querySnapshot -> {
//                    foodList.clear();  // Clear existing data before adding new
//                    for (QueryDocumentSnapshot doc : querySnapshot) {
//                        FoodModel food = doc.toObject(FoodModel.class);  // Convert Firestore document to FoodModel
//                        foodList.add(food);
//                    }
//                    adapter.notifyDataSetChanged();  // Notify adapter of new data
//                })
//                .addOnFailureListener(e -> Log.e("FirestoreTest", "Failed to load Food data", e));  // Log any errors
//    }

    public void goToTables(View view) {
        Intent i = new Intent(this, StaffTableListActivity.class);
        startActivity(i);
    }
}
