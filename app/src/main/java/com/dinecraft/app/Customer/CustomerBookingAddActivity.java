package com.dinecraft.app.Customer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.Config;
import com.dinecraft.app.Customer.CustomerMainActivity;
import com.dinecraft.app.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomerBookingAddActivity extends BaseActivity {

    private EditText  edtSeats,  edtContact, edtMemo;
    private Button btnAdd, btnCancel;
    private Spinner spnTableName, spnTimeslot;
    private ImageView iv_datepicker, iv_delete;
    private TextView edtDate;
    private String prefName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booking_add);
        setupCusNav();
        setupBottomNav();
        setupTopProfile();


        prefName = getIntent().getStringExtra("pref_name");
        if (prefName == null) prefName = "Temp-Customer";

        edtDate = findViewById(R.id.edt_date);
        edtDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
        iv_datepicker = findViewById(R.id.iv_datepicker);
        iv_delete = findViewById(R.id.iv_datedelete);
        spnTimeslot = findViewById(R.id.spn_timeslot);
        edtSeats = findViewById(R.id.edt_seats);
        spnTableName = findViewById(R.id.spn_table_name);
        edtContact = findViewById(R.id.edt_contact_number);
        edtMemo = findViewById(R.id.edt_memo);

        btnAdd = findViewById(R.id.btn_add);
        btnCancel = findViewById(R.id.btn_cancel);

        // Initialize the spinner for timeslots
        Config.init_spinner(spnTimeslot, R.array.staff_timeslot, this);
        // Initialize the spinner for tables, callback is to set the booking data
        Config.getInstance().init_table_spinner(spnTableName, this, null);

        btnAdd.setOnClickListener(v -> {
            String id = UUID.randomUUID().toString();
            String dateStr = edtDate.getText().toString().trim();
            int timeslot = parseIntSafe(spnTimeslot.getSelectedItem().toString().trim());
            int seats = parseIntSafe(edtSeats.getText().toString().trim());
            String tableName = spnTableName.getSelectedItem().toString().trim();
            String contact = edtContact.getText().toString().trim();
            String memo = edtMemo.getText().toString().trim();

            Map<String, Object> data = new HashMap<>();
            data.put("booking_id", id);
            data.put("pref_name", prefName);
            data.put("timeslot", timeslot);
            data.put("seat_required", seats);
            data.put("table_name", tableName);
            data.put("contact_number", contact);
            data.put("memo", memo);
            // store date as timestamp if empty: now
            data.put("date", dateStr);

            FirebaseFirestore.getInstance().collection("bookings").document(id).set(data)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Booking added", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, CustomerMainActivity.class);
                        i.putExtra("pref_name", prefName);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show());
        });

        btnCancel.setOnClickListener(v -> {
            Intent i = new Intent(this, CustomerMainActivity.class);
            i.putExtra("pref_name", prefName);
            startActivity(i);
            finish();
        });

        iv_datepicker.setOnClickListener(v-> showDatePicker(edtDate));
        iv_delete.setOnClickListener(v-> edtDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())));
    }

    private int parseIntSafe(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }

    private void showDatePicker(TextView tvDate) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (DatePicker view, int year1, int month1, int dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    tvDate.setText(selectedDate);
                },
                year, month, day
        );
        dialog.show();
    }
}
