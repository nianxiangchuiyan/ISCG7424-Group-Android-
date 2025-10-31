package com.dinecraft.app.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.R;
import com.dinecraft.app.staff.FoodModel;
import com.dinecraft.app.staff.FoodRVAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CusFoodListActivity extends BaseActivity {
    private RecyclerView rv;
    private FoodRVAdapter adapter;
    private List<FoodModel> foodList = new ArrayList<>();
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_food_list);
        setupCusNav();
        setupBottomNav();
        setupTopProfile();

        rv = findViewById(R.id.rv_staff_food_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodRVAdapter(this, foodList);
        rv.setAdapter(adapter);

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CusFoodAddActivity.class)));

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
