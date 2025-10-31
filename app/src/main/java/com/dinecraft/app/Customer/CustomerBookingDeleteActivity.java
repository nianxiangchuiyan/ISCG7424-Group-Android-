package com.dinecraft.app.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.Customer.CustomerMainActivity;
import com.dinecraft.app.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerBookingDeleteActivity extends BaseActivity {

    private EditText edtBookingId;
    private Button btnDelete, btnCancel;
    private String prefName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booking_delete);
        setupCusNav();
        setupBottomNav();
        setupTopProfile();

        prefName = getIntent().getStringExtra("pref_name");
        if (prefName == null) prefName = "Temp-Customer";

        edtBookingId = findViewById(R.id.edt_booking_id);
        btnDelete = findViewById(R.id.btn_delete);
        btnCancel = findViewById(R.id.btn_cancel);

        btnDelete.setOnClickListener(v -> {
            String id = edtBookingId.getText().toString().trim();
            if (id.isEmpty()) {
                Toast.makeText(this, "Enter booking id", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseFirestore.getInstance().collection("bookings").document(id).delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Booking deleted", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, CustomerMainActivity.class);
                        i.putExtra("pref_name", prefName);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show());
        });

        btnCancel.setOnClickListener(v -> {
            Intent i = new Intent(this, CustomerMainActivity.class);
            i.putExtra("pref_name", prefName);
            startActivity(i);
            finish();
        });
    }
}
