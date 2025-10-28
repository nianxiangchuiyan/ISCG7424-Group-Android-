package com.dinecraft.app.staff;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.Booking;
import com.dinecraft.app.Config;
import com.dinecraft.app.Customer.CustomerMainActivity;
import com.dinecraft.app.R;
import com.dinecraft.app.Table;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StaffBookingAddActivity extends BaseActivity {
    TextView tv_date;
    private EditText edtSeats, edtContact, edtMemo, edtPrefName;
    private Spinner spnTableName, spnTimeslot;
    private ImageView iv_datepicker, iv_delete;
    private Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_booking_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupBottomNav();
        setupStaffNav();
        setupTopProfile();

        tv_date = findViewById(R.id.tv_date);
        tv_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
        iv_datepicker = findViewById(R.id.iv_datepicker);
        iv_delete = findViewById(R.id.iv_datedelete);
        spnTimeslot = findViewById(R.id.spn_timeslot);
        edtPrefName = findViewById(R.id.edt_prefname);
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
        iv_datepicker.setOnClickListener(v-> showDatePicker(tv_date));
        iv_delete.setOnClickListener(v-> tv_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())));

        btnCancel.setOnClickListener(v -> {
            Intent i = new Intent(this, StaffMainActivity.class);
            startActivity(i);
            finish();
        });

        btnAdd.setOnClickListener(v -> {
            Booking booking = new Booking();

            booking.setDate(tv_date.getText().toString().trim());
            booking.setTimeslot(parseIntSafe(spnTimeslot.getSelectedItem().toString().trim()));
            booking.setSeat_required(parseIntSafe(edtSeats.getText().toString().trim()));
            booking.setPref_name(edtPrefName.getText().toString().trim());
            booking.setTable_name(spnTableName.getSelectedItem().toString().trim());
            booking.setContact_number( edtContact.getText().toString().trim());
            booking.setMemo(edtMemo.getText().toString().trim());

            FirebaseFirestore.getInstance().collection("bookings")
                    .add(booking)
                    .addOnSuccessListener(documentReference -> {
                        documentReference.update("booking_id", documentReference.getId());
                        Toast.makeText(this, "Booking successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, StaffMainActivity.class);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show());

        });

    }

    private int parseIntSafe(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }

    private void showDatePicker(TextView tvDate) {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 创建日期选择器对话框
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