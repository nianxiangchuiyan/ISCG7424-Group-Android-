package com.dinecraft.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dinecraft.app.Customer.CustomerMainActivity;
import com.dinecraft.app.admin.AdminMainActivity;
import com.dinecraft.app.staff.StaffMainActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        testdb();
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //init button nav bar
        setupBottomNav();
        setupTopProfile();
    }


    //Temp function to go to staff main activity
//    public void goStaff(View view) {
//        Intent i = new Intent(this, StaffMainActivity.class);
//        startActivity(i);
//    }
//    public void goAdmin(View view) {
//        Intent i = new Intent(this, AdminMainActivity.class);
//        startActivity(i);
//    }
//    public void goToCustomerMain(View view) {
//        Intent i = new Intent(this, CustomerMainActivity.class);
//        i.putExtra("pref_name", "Temp-Customer");
//        startActivity(i);
//    }

    private void testdb() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //trying names
        String[] collections = {"staff", "orders","Bookings", "users","Foods","Food","Tables"};

        for (String collectionName : collections) {
            db.collection(collectionName)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (querySnapshot.isEmpty()) {
                            Log.d("FirestoreTest", "No documents found in " + collectionName);
                        } else {
                            for (QueryDocumentSnapshot doc : querySnapshot) {
                                Log.d("FirestoreTest", "Collection: " + collectionName +
                                        " | DocID: " + doc.getId() +
                                        " | Data: " + doc.getData());
                            }
                        }
                    })
                    .addOnFailureListener(e ->
                            Log.e("FirestoreTest", "Failed to read from " + collectionName, e));
        }
    }

    public void Updata(View view) {
        Intent i = new Intent(this, UploadDataActivity.class);
        startActivity(i);
    }
}