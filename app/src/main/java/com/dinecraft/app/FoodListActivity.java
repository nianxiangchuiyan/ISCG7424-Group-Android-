package com.dinecraft.app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity {

    private List<Food> allFoods;
    private List<Food> filteredFoods;
    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_foods);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView title = findViewById(R.id.tvCategoryTitle);

        String categoryName = getIntent().getStringExtra("categoryName");
        title.setText(categoryName);

        // Hard-coded foods must add with image(replace with Firestore later)
        allFoods = new ArrayList<>();
        allFoods.add(new Food("1", "Ender Pearl Pasta", "Meal", "Noodles and Pearls from another Dimension", 10.99, "ender_pearl_pasta"));
        allFoods.add(new Food("2", "Nether Sandwich", "Meal", "A sandwich with pork procured from Hoglins", 12.99, "nethersandwich"));
        allFoods.add(new Food("3", "Glowberry Salad", "Meal", "Glowberrys from lush caves", 11.49 , "glowberrysalad"));
        allFoods.add(new Food("4", "Chorus Fruit Punch", "Drinks", "Carbonated drink made from Chorus fruit", 2.99, "chorusfruit"));
        allFoods.add(new Food("5", "Cheese Cake", "Desserts", "Cheese from a cow and Chocolate with green icing ontop", 5.99,"minecraftcheesecake"));
//3
        filteredFoods = new ArrayList<>();
        for (Food f : allFoods) {
            if (f.getCategory().equalsIgnoreCase(categoryName)) {
                filteredFoods.add(f);
            }
        }

        foodAdapter = new FoodAdapter(filteredFoods);
        recyclerView.setAdapter(foodAdapter);
    }
}
