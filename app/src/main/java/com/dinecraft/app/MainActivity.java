package com.dinecraft.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<Category> categoryList;
    private List<Category> filteredList;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        setupBottomNav();
        setupTopProfile();

        RecyclerView recyclerView = findViewById(R.id.recycler_categories);
        EditText searchBar = findViewById(R.id.search_bar);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Category items with images
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Sides", R.drawable.soup));
        categoryList.add(new Category("Meals", R.drawable.burger));
        categoryList.add(new Category("Drinks", R.drawable.soda));
        categoryList.add(new Category("Desserts", R.drawable.dessert));

        filteredList = new ArrayList<>(categoryList);

        adapter = new CategoryAdapter(filteredList, category -> {
            Intent intent = new Intent(MainActivity.this, FoodListActivity.class);
            intent.putExtra("categoryName", category.getName());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Search logic
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCategories(s.toString());
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void filterCategories(String text) {
        filteredList.clear();
        for (Category c : categoryList) {
            if (c.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(c);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
