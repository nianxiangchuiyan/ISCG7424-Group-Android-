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

                        new Food("Food01", "Golden Carrot Soup", "Mains", "Creamy carrot soup made with golden carrots and pixel herbs.", 9.50),
                        new Food("Food02", "Honey-Glazed Porkchop Burger", "Mains", "Grilled porkchop burger topped with honey glaze and voxel lettuce.", 11.25),
                        new Food("Food03", "Chorus Fruit Salad", "Sides", "A mix of chorus fruit cubes, melon, and glow berries that shimmer in light.", 7.25),
                        new Food("Food04", "Glow Berry Smoothie", "Drinks", "Blended glow berries with milk and a hint of sugar cane syrup.", 5.99),
                        new Food("Food05", "Nether Steak Sandwich", "Mains", "Charred steak with magma mayo on toasted bread blocks.", 12.50),
                        new Food("Food06", "Pumpkin Pie Parfait", "Desserts", "Layers of spiced pumpkin pie cream and cookie crumbs.", 6.75),
                        new Food("Food07", "Sweet Berry Tart", "Desserts", "Flaky crust filled with sweet berry jam and topped with sugar pixels.", 6.25),
                        new Food("Food08", "Beetroot Burger", "Mains", "Red beetroot patty with golden carrot relish and fresh leaves.", 10.49),
                        new Food("Food09", "Mushroom Stew Bowl", "Mains", "Hearty stew of red and brown mushrooms served in a stone bowl.", 8.99),
                        new Food("Food10", "Apple Cider Float", "Drinks", "Sparkling apple cider topped with vanilla ice cream.", 5.50),
                        new Food("Food11", "Golden Apple Tart", "Desserts", "Shiny golden apple filling baked into a flaky crust.", 7.99),
                        new Food("Food12", "Ender Curry", "Mains", "Purple curry infused with chorus fruit and end spice.", 12.75),
                        new Food("Food13", "Slime Cube Jelly", "Desserts", "Bouncy green gelatin dessert that jiggles like slime.", 4.99),
                        new Food("Food14", "Villager Veggie Platter", "Sides", "Carrot sticks, beet cubes, and melon bites served fresh.", 6.25),
                        new Food("Food15", "Redstone Energy Drink", "Drinks", "Electric red drink said to boost your mining speed.", 4.50),
                        new Food("Food16", "Lava Hot Wings", "Mains", "Spicy wings cooked in blaze powder sauce.", 10.75),
                        new Food("Food17", "Snow Golem Sundae", "Desserts", "Vanilla ice cream topped with snow powder and cocoa bits.", 6.10),
                        new Food("Food18", "Creeper Curry Bowl", "Mains", "Green curry with an explosive flavor kick.", 11.20),
                        new Food("Food19", "Enderman Espresso", "Drinks", "Strong black coffee with teleport-level energy.", 4.30),
                        new Food("Food20", "Tropical Fish Tacos", "Mains", "Soft tacos filled with fried tropical fish and kelp slaw.", 9.75),
                        new Food("Food21", "Golden Carrot Stir-Fry", "Mains", "Golden carrots tossed with veggies and honey glaze.", 10.50),
                        new Food("Food22", "Melon Frost Float", "Drinks", "Chilled melon soda with glow berry ice cubes.", 5.10),
                        new Food("Food23", "Blaze Pepper Noodles", "Mains", "Fiery noodles cooked with blaze rod spice and magma oil.", 11.95),
                        new Food("Food24", "Nether Quartz Brownie", "Desserts", "Chunky chocolate brownie with white quartz drizzle.", 5.80),
                        new Food("Food25", "Potion of Healing Mocktail", "Drinks", "Refreshing red fruit mix inspired by healing potions.", 5.25),
                        new Food("Food26", "Ghast Marshmallow Puff", "Desserts", "Toasted marshmallow dessert that melts in your mouth.", 4.60),
                        new Food("Food27", "Enchanted Apple Crumble", "Desserts", "Apple crumble sprinkled with enchanted sugar dust.", 7.25),
                        new Food("Food28", "Warden’s Deep Stew", "Mains", "Dark and rich beef stew from the depths of the Deep Dark.", 13.25),
                        new Food("Food29", "Axolotl Bubble Tea", "Drinks", "Pastel-colored boba tea with glowing tapioca pearls.", 6.40),
                        new Food("Food30", "Frost Walker Ice Cream", "Desserts", "Blue-tinted ice cream that chills you to the core.", 6.70)


        );

        for (Food food : foodList) {
            db.collection("Food").document(food.getId())
                    .set(food)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "✅ Added " + food.getName()))
                    .addOnFailureListener(e -> Log.e("Firebase", "❌ Error adding " + food.getName(), e));
        }
    }
}
