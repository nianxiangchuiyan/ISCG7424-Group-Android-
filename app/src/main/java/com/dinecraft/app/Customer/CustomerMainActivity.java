package com.dinecraft.app.Customer;

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

public class CustomerMainActivity extends BaseActivity {

    private RecyclerView rv;
    private BookingRVAdapter adapter;
    private List<Booking> list = new ArrayList<>();
    private Button btnNew, btnDelete,btnCancel;;

    private String prefName;
    private boolean isDeleteMode = false; // Track delete mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        setupCusNav(); // reuse existing nav include

        rv = findViewById(R.id.rv_customer_bookings);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookingRVAdapter(this, list, booking -> {
            if (isDeleteMode) {
                // Delete the booking if delete mode is active
                deleteBooking(booking.getBooking_id());
            } else {
                // Edit the booking if delete mode is not active
                Intent i = new Intent(this, CustomerBookingDetailActivity.class);
                i.putExtra("doc_id", booking.getBooking_id());
                i.putExtra("pref_name", getIntent().getStringExtra("pref_name"));
                startActivity(i);
            }
        });

        rv.setAdapter(adapter);
        btnNew = findViewById(R.id.btn_new_booking);
        btnDelete = findViewById(R.id.btn_delete_booking);
        btnNew.setOnClickListener(v -> {
            Intent i = new Intent(this, CustomerBookingAddActivity.class);
            i.putExtra("pref_name", prefName);
            startActivity(i);
        });

        btnDelete.setOnClickListener(v -> {
            if(!isDeleteMode){
            isDeleteMode = !isDeleteMode;
            updateUIForDeleteMode(isDeleteMode);}
            else{isDeleteMode = false;
                updateUIForDeleteMode(false);}
        });


        loadBookings();
    }

    private void updateUIForDeleteMode(boolean isDeleteMode) {
        // Update UI to show delete buttons on cards and change button text
        if (isDeleteMode) {
            btnDelete.setText("Cancel");
            btnDelete.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // Green
        } else {
            btnDelete.setText("Delete Booking");
            btnDelete.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // Red
        }

        // Notify adapter to toggle delete buttons on cards
        adapter.notifyDataSetChanged();
    }

    private void loadBookings() {
        FirebaseFirestore.getInstance().collection("bookings")
                .whereEqualTo("pref_name", getIntent().getStringExtra("pref_name"))
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

    private void deleteBooking(String bookingId) {
        FirebaseFirestore.getInstance().collection("bookings").document(bookingId).delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove the booking from the list
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getBooking_id().equals(bookingId)) {
                            list.remove(i);
                            break;
                        }
                    }
                    adapter.notifyDataSetChanged(); // Update the list after deletion
                });
    }
}