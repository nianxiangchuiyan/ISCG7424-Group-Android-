package com.dinecraft.app.staff;

import android.app.AlertDialog;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class StaffFoodDetailActivity extends BaseActivity {

    private EditText edt_name, edt_category, edt_price, edt_description;
    private Button btn_update, btn_cancel, btn_delete;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_food_detail);
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
        btn_update = findViewById(R.id.btn_update);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_delete = findViewById(R.id.btn_delete);

        Intent i = getIntent();
        id = i.getStringExtra("id");
        edt_name.setText(i.getStringExtra("name"));
        edt_category.setText(i.getStringExtra("category"));
        edt_price.setText(String.valueOf(i.getDoubleExtra("price", 0)));
        edt_description.setText(i.getStringExtra("description"));

        btn_update.setOnClickListener(v -> {
            String name = edt_name.getText().toString().trim();
            String category = edt_category.getText().toString().trim();
            String description = edt_description.getText().toString().trim();
            double price = 0;
            try { price = Double.parseDouble(edt_price.getText().toString().trim()); } catch (Exception ignored) {}

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FoodModel updated = new FoodModel();
            // FoodModel has only getters; Firestore can update via map
            db.collection("Food").document(id)
                    .update("name", name, "category", category, "description", description, "price", price)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Food updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), StaffFoodListActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show());
        });

        btn_delete.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Delete Food")
                    .setMessage("Are you sure you want to delete this food?")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", (dialog1, which) -> {

                                FirebaseFirestore.getInstance().collection("Food").document(id).delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "Food deleted", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), StaffFoodListActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show());
                            })
                    .create();
            dialog.show();
        });

        btn_cancel.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), StaffFoodListActivity.class));
            finish();
        });
    }
}
