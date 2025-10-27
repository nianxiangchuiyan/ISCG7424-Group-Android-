package com.dinecraft.app.staff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StaffFoodListActivity extends BaseActivity {
    private RecyclerView rv;
    private FoodRVAdapter adapter;
    private List<FoodModel> foodList = new ArrayList<>();
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_food_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupBottomNav();
        setupStaffNav();
        setupTopProfile();

        rv = findViewById(R.id.rv_staff_food_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodRVAdapter(this, foodList);
        rv.setAdapter(adapter);

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), StaffFoodAddActivity.class)));

        loadFoods();
    }

    private void loadFoods() {
        FirebaseFirestore.getInstance().collection("Food")
                .get()
                .addOnSuccessListener(snap -> {
                    foodList.clear();
                    for (QueryDocumentSnapshot doc : snap) {
                        FoodModel f = doc.toObject(FoodModel.class);
                        foodList.add(f);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
