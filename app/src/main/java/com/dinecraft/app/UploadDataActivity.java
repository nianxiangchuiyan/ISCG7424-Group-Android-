package com.dinecraft.app;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Arrays;
import java.util.List;

public class UploadDataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<Food> foodList = Arrays.asList(
                new Food("Food01", "Block Burger", "Mains", "A chunky cube-shaped burger with pixel cheese and lettuce.", 9.99),
                new Food("Food02", "Pixel Pizza", "Mains", "Square pizza slices topped with digital pepperoni.", 11.49),
                new Food("Food03", "Voxel Fries", "Sides", "Crunchy cubed fries served in a blocky basket.", 4.50),
                new Food("Food04", "Craft Cola", "Drinks", "Classic cola brewed with bits and bytes.", 3.20),
                new Food("Food05", "Glowberry Shake", "Desserts", "A neon purple milkshake glowing softly in the dark.", 6.99)
        );

        for (Food food : foodList) {
            db.collection("Food").document(food.getId())
                    .set(food)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "✅ Added " + food.getName()))
                    .addOnFailureListener(e -> Log.e("Firebase", "❌ Error adding " + food.getName(), e));
        }
    }
}
