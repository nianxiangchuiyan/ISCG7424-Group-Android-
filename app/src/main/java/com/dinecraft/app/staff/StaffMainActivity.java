package com.dinecraft.app.staff;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.Config;
import com.dinecraft.app.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StaffMainActivity extends BaseActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout first
        setContentView(R.layout.activity_staff_main);

        // Now find the RecyclerView after setContentView
        recyclerView = findViewById(R.id.rv_staff_booking);  // Ensure this ID matches the RecyclerView in your XML layout

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView with GridLayoutManager (5 columns for a table-like layout)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1)); // 5 columns for ID, Name, Category, Price, and Description

        // Set window insets for the UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the bottom navigation bar
        setupBottomNav();

        // Initialize the spinner for timeslots
        Config.init_spinner(findViewById(R.id.spn_timeslot), R.array.staff_timeslot, this);
        // Initialize the spinner for tables
        Config.getInstance().init_table_spinner(findViewById(R.id.spn_table), this);


    }

    // Load data for Food from Firestore
    public void loadFoodData(View view) {
        List<FoodModel> foodList = new ArrayList<>();
        FoodAdapter adapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);

        db.collection("Food")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    foodList.clear();  // Clear existing data before adding new
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        FoodModel food = doc.toObject(FoodModel.class);  // Convert Firestore document to FoodModel
                        foodList.add(food);
                    }
                    adapter.notifyDataSetChanged();  // Notify adapter of new data
                })
                .addOnFailureListener(e -> Log.e("FirestoreTest", "Failed to load Food data", e));  // Log any errors
    }

    public void goToTables(View view) {
        Intent i = new Intent(this, StaffTableListActivity.class);
        startActivity(i);
    }
}
