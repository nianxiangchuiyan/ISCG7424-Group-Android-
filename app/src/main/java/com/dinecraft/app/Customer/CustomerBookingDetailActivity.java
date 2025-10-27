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

import java.util.Date;

public class CustomerBookingDetailActivity extends BaseActivity {

    private EditText edtTimeslot, edtSeats, edtTableName, edtContact, edtMemo;
    private Button btnUpdate, btnCancel;
    private String docId, prefName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booking_detail);
        setupStaffNav();

        docId = getIntent().getStringExtra("doc_id");
        prefName = getIntent().getStringExtra("pref_name");
        if (prefName == null) prefName = "Temp-Customer";

        edtTimeslot = findViewById(R.id.edt_timeslot);
        edtSeats = findViewById(R.id.edt_seats);
        edtTableName = findViewById(R.id.edt_table_name);
        edtContact = findViewById(R.id.edt_contact_number);
        edtMemo = findViewById(R.id.edt_memo);

        btnUpdate = findViewById(R.id.btn_update);
        btnCancel = findViewById(R.id.btn_cancel);

        // (Optional) could load doc to prefill here; keeping simple

        btnUpdate.setOnClickListener(v -> {
            int timeslot = parseIntSafe(edtTimeslot.getText().toString().trim());
            int seats = parseIntSafe(edtSeats.getText().toString().trim());
            String tableName = edtTableName.getText().toString().trim();
            String contact = edtContact.getText().toString().trim();
            String memo = edtMemo.getText().toString().trim();

            FirebaseFirestore.getInstance().collection("bookings").document(docId)
                    .update("timeslot", timeslot,
                            "seat_required", seats,
                            "table_name", tableName,
                            "contact_number", contact,
                            "memo", memo,
                            "pref_name", prefName)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Booking updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, CustomerMainActivity.class);
                        i.putExtra("pref_name", prefName);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show());
        });

        btnCancel.setOnClickListener(v -> {
            Intent i = new Intent(this, CustomerMainActivity.class);
            i.putExtra("pref_name", prefName);
            startActivity(i);
            finish();
        });
    }

    private int parseIntSafe(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }
}
