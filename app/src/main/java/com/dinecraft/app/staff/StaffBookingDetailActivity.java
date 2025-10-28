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

public class StaffBookingDetailActivity extends BaseActivity {
    Booking curBooking;
    TextView tv_date;
    private EditText edtSeats, edtContact, edtMemo, edtPrefName;
    private Spinner spnTableName, spnTimeslot;
    private ImageView iv_datepicker, iv_delete;

    private boolean firsttimeTable =true;
    private boolean firsttimeTimeslot =true;
    private Button btnUpdate, btnCancel;
    private String docId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_booking_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupBottomNav();
        setupStaffNav();
        setupTopProfile();

        curBooking = Config.getInstance().getStaffBooking();

        tv_date = findViewById(R.id.tv_date);
        iv_datepicker = findViewById(R.id.iv_datepicker);
        iv_delete = findViewById(R.id.iv_datedelete);
        spnTimeslot = findViewById(R.id.spn_timeslot);
        edtPrefName = findViewById(R.id.edt_prefname);
        edtSeats = findViewById(R.id.edt_seats);
        spnTableName = findViewById(R.id.spn_table_name);
        edtContact = findViewById(R.id.edt_contact_number);
        edtMemo = findViewById(R.id.edt_memo);

        btnUpdate = findViewById(R.id.btn_update);
        btnCancel = findViewById(R.id.btn_cancel);

        // Initialize the spinner for timeslots
        Config.init_spinner(spnTimeslot, R.array.staff_timeslot, this);
        // Initialize the spinner for tables, callback is to set the booking data
        Config.getInstance().init_table_spinner(spnTableName, this, () -> {
            ArrayAdapter<Table> adapter;
            if(curBooking!=null){
                docId = curBooking.getBooking_id();
                tv_date.setText(curBooking.getDate());
                spnTimeslot.setSelection(curBooking.getTimeslot());
                edtPrefName.setText(curBooking.getPref_name());
                edtSeats.setText(String.valueOf(curBooking.getSeat_required()));
                adapter = (ArrayAdapter<Table>) spnTableName.getAdapter();
                int position = -1;
                for (int i = 0; i < adapter.getCount(); i++) {
                    Log.d("DDDDD", "Index " + i + ": '" + adapter.getItem(i).getName() + "'");
                    if (adapter.getItem(i).getName().equals(curBooking.getTable_name())) {
                        position = i;
                        break;
                    }
                }
                Log.d("DDDDD", "(-----------------------------)");
                //int position = adapter.getPosition(curBooking.getTable_name().trim());
                //int position = adapter.getPosition("BigRoundTable");
                if(position >= 0) spnTableName.setSelection(position);
                edtContact.setText(curBooking.getContact_number());
                edtMemo.setText(curBooking.getMemo());
            }
        });
        iv_datepicker.setOnClickListener(v-> showDatePicker(tv_date));
        iv_delete.setOnClickListener(v-> tv_date.setText("dd/mm/yyyy"));

        btnUpdate.setOnClickListener(v -> {
            String date = tv_date.getText().toString().trim();
            int timeslot = parseIntSafe(spnTimeslot.getSelectedItem().toString().trim());
            int seats = parseIntSafe(edtSeats.getText().toString().trim());
            String prefName = edtPrefName.getText().toString().trim();
            String tableName = spnTableName.getSelectedItem().toString().trim();
            String contact = edtContact.getText().toString().trim();
            String memo = edtMemo.getText().toString().trim();

            FirebaseFirestore.getInstance().collection("bookings").document(docId)
                    .update("date",date,
                            "timeslot", timeslot,
                            "seat_required", seats,
                            "table_name", tableName,
                            "contact_number", contact,
                            "memo", memo,
                            "pref_name", prefName)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Booking updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, StaffMainActivity.class);
                        //i.putExtra("pref_name", prefName);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show());
        });

        btnCancel.setOnClickListener(v -> {
            Intent i = new Intent(this, StaffMainActivity.class);
            startActivity(i);
            finish();
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

    public interface onDataLoadedListener {
        void onDataLoaded();
    }



}