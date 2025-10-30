package com.dinecraft.app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        RecyclerView recyclerView = findViewById(R.id.recycler_categories);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Soup", R.drawable.soup));
        categoryList.add(new Category("Burgers", R.drawable.burger));
        categoryList.add(new Category("Drinks", R.drawable.soda));
        categoryList.add(new Category("Desserts", R.drawable.dessert));

        CategoryAdapter adapter = new CategoryAdapter(categoryList, category ->
                Toast.makeText(this, "Clicked " + category.getName(), Toast.LENGTH_SHORT).show()
        );

        recyclerView.setAdapter(adapter);
    }
}
