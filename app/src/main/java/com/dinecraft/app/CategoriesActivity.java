package com.dinecraft.app;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private List<Category> categoryList;
    private List<Category> filteredList;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        RecyclerView recyclerView = findViewById(R.id.recycler_categories);
        SearchView searchView = findViewById(R.id.searchView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        categoryList = new ArrayList<>();
        categoryList.add(new Category("Soup", R.drawable.soup));
        categoryList.add(new Category("Meals", R.drawable.burger));
        categoryList.add(new Category("Drinks", R.drawable.soda));
        categoryList.add(new Category("Desserts", R.drawable.dessert));

        filteredList = new ArrayList<>(categoryList);

        adapter = new CategoryAdapter(filteredList, category ->
                Toast.makeText(this, "Clicked " + category.getName(), Toast.LENGTH_SHORT).show()
        );

        recyclerView.setAdapter(adapter);

        // Search filter listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // We filter as user types, no need to submit
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCategories(newText);
                return true;
            }
        });
    }

    private void filterCategories(String text) {
        filteredList.clear();
        text = text.toLowerCase();

        for (Category c : categoryList) {
            if (c.getName().toLowerCase().contains(text)) {
                filteredList.add(c);
            }
        }

        adapter.notifyDataSetChanged();
    }
}
