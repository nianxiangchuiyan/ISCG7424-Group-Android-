package com.dinecraft.app.staff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StaffFoodAddActivity extends BaseActivity {

    private EditText edt_name, edt_category, edt_price, edt_description;
    private Button btn_add, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_food_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupBottomNav();
        setupStaffNav();
        setupTopProfile();

        edt_name = findViewById(R.id.edt_food_name);
        edt_category = findViewById(R.id.edt_food_category);
        edt_price = findViewById(R.id.edt_food_price);
        edt_description = findViewById(R.id.edt_food_description);
        btn_add = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_add.setOnClickListener(v -> {
            String id = UUID.randomUUID().toString();
            String name = edt_name.getText().toString().trim();
            String category = edt_category.getText().toString().trim();
            String description = edt_description.getText().toString().trim();
            double price = 0;
            try { price = Double.parseDouble(edt_price.getText().toString().trim()); } catch (Exception ignored) {}

            Map<String, Object> data = new HashMap<>();
            data.put("id", id);
            data.put("name", name);
            data.put("category", category);
            data.put("description", description);
            data.put("price", price);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Food").document(id).set(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Food added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), StaffFoodListActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show());
        });

        btn_cancel.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), StaffFoodListActivity.class));
            finish();
        });
    }
}
