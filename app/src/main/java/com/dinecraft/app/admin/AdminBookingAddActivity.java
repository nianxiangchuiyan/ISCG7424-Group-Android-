package com.dinecraft.app.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdminBookingAddActivity extends BaseActivity {

    private EditText edtTimeslot, edtSeats, edtTableName, edtContact, edtMemo;
    private Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_add);
        setupStaffNav();

        edtTimeslot = findViewById(R.id.edt_timeslot);
        edtSeats = findViewById(R.id.edt_seats);
        edtTableName = findViewById(R.id.edt_table_name);
        edtContact = findViewById(R.id.edt_contact_number);
        edtMemo = findViewById(R.id.edt_memo);

        btnAdd = findViewById(R.id.btn_add);
        btnCancel = findViewById(R.id.btn_cancel);

        btnAdd.setOnClickListener(v -> {
            addBooking();
        });

        btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
            finish();
        });
    }

    private void addBooking() {
        String id = UUID.randomUUID().toString();
        int timeslot = Integer.parseInt(edtTimeslot.getText().toString().trim());
        int seats = Integer.parseInt(edtSeats.getText().toString().trim());
        String tableName = edtTableName.getText().toString().trim();
        String contact = edtContact.getText().toString().trim();
        String memo = edtMemo.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put("booking_id", id);
        data.put("timeslot", timeslot);
        data.put("seat_required", seats);
        data.put("table_name", tableName);
        data.put("contact_number", contact);
        data.put("memo", memo);
        data.put("pref_name", "Admin"); // Default to Admin or any user role

        FirebaseFirestore.getInstance().collection("bookings").document(id).set(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Booking added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show());
    }
}
