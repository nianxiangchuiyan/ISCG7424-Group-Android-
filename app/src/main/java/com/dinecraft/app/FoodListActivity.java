package com.dinecraft.app;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity {

    private List<Food> allFoods;
    private List<Food> filteredFoods;
    private FoodAdapter foodAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_foods);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView title = findViewById(R.id.tvCategoryTitle);
        SearchView searchView = findViewById(R.id.searchViewFood);

        String categoryName = getIntent().getStringExtra("categoryName");
        title.setText(categoryName);

        db = FirebaseFirestore.getInstance();
        allFoods = new ArrayList<>();
        filteredFoods = new ArrayList<>();

        foodAdapter = new FoodAdapter(filteredFoods);
        recyclerView.setAdapter(foodAdapter);

        // ✅ Fetch from Firestore
        db.collection("Food")
                .whereEqualTo("category", categoryName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Food food = doc.toObject(Food.class);
                        allFoods.add(food);
                        filteredFoods.add(food);
                    }
                    foodAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    title.setText("Error loading food");
                });

        // ✅ Search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredFoods.clear();
                for (Food f : allFoods) {
                    if (f.getName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredFoods.add(f);
                    }
                }
                foodAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
}
