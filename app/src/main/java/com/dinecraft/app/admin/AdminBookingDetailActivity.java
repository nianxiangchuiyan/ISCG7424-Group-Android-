package com.dinecraft.app.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.Booking;
import com.dinecraft.app.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminBookingDetailActivity extends BaseActivity {

    private EditText edtTimeslot, edtSeats, edtTableName, edtContact, edtMemo;
    private Button btnUpdate, btnCancel;
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_detail);
        setupStaffNav();

        docId = getIntent().getStringExtra("doc_id");

        edtTimeslot = findViewById(R.id.edt_timeslot);
        edtSeats = findViewById(R.id.edt_seats);
        edtTableName = findViewById(R.id.edt_table_name);
        edtContact = findViewById(R.id.edt_contact_number);
        edtMemo = findViewById(R.id.edt_memo);

        btnUpdate = findViewById(R.id.btn_update);
        btnCancel = findViewById(R.id.btn_cancel);

        // Load data
        loadBookingDetails();

        btnUpdate.setOnClickListener(v -> {
            updateBooking();
        });

        btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
            finish();
        });
    }

    private void loadBookingDetails() {
        FirebaseFirestore.getInstance().collection("bookings").document(docId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Booking booking = doc.toObject(Booking.class);
                        edtTimeslot.setText(String.valueOf(booking.getTimeslot()));
                        edtSeats.setText(String.valueOf(booking.getSeat_required()));
                        edtTableName.setText(booking.getTable_name());
                        edtContact.setText(booking.getContact_number());
                        edtMemo.setText(booking.getMemo());
                    }
                });
    }

    private void updateBooking() {
        int timeslot = Integer.parseInt(edtTimeslot.getText().toString().trim());
        int seats = Integer.parseInt(edtSeats.getText().toString().trim());
        String tableName = edtTableName.getText().toString().trim();
        String contact = edtContact.getText().toString().trim();
        String memo = edtMemo.getText().toString().trim();

        FirebaseFirestore.getInstance().collection("bookings").document(docId)
                .update("timeslot", timeslot, "seat_required", seats, "table_name", tableName, "contact_number", contact, "memo", memo)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Booking updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                    finish();
                });
    }
}
