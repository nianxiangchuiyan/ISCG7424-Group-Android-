package com.dinecraft.app.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.Booking;
import com.dinecraft.app.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends BaseActivity {

    private RecyclerView rv;
    private AdminBookingRVAdapter adapter;
    private List<Booking> list = new ArrayList<>();
    private Button btnNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        setupStaffNav(); // reuse existing nav include

        rv = findViewById(R.id.rv_admin_bookings);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminBookingRVAdapter(this, list, booking -> {
            Intent i = new Intent(this, AdminBookingDetailActivity.class);
            i.putExtra("doc_id", booking.getBooking_id());
            startActivity(i);
        });
        rv.setAdapter(adapter);

        btnNew = findViewById(R.id.btn_new_booking);
        btnNew.setOnClickListener(v -> {
            Intent i = new Intent(this, AdminBookingAddActivity.class);
            startActivity(i);
        });

        loadBookings();
    }

    private void loadBookings() {
        FirebaseFirestore.getInstance().collection("bookings")
                .get()
                .addOnSuccessListener(snap -> {
                    list.clear();
                    for (QueryDocumentSnapshot d : snap) {
                        Booking b = d.toObject(Booking.class);
                        b.setBooking_id(d.getId()); // Set document id for updates
                        list.add(b);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
