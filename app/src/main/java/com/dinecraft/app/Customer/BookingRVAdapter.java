package com.dinecraft.app.Customer;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dinecraft.app.Booking;
import com.dinecraft.app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingRVAdapter extends RecyclerView.Adapter<BookingRVAdapter.BookingViewHolder> {

    private final Context context;
    private final List<Booking> bookings;
    private final OnBookingClick listener;
    private final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private boolean isDeleteMode = false; // Track whether in delete mode

    public interface OnBookingClick {
        void onClick(Booking booking);
    }

    public BookingRVAdapter(Context context, List<Booking> bookings, OnBookingClick listener) {
        this.context = context;
        this.bookings = bookings;
        this.listener = listener;
        this.isDeleteMode = isDeleteMode;
    }

    @NonNull @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_booking, parent, false);
        return new BookingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking b = bookings.get(position);
        holder.tv_title.setText(b.getTable_name() == null ? "Booking" : b.getTable_name());
        String dateStr = b.getDate() != null ? fmt.format(b.getDate()) : ""+getmyDate();
        holder.tv_sub.setText("Date: " + dateStr + " • Slot: " + b.getTimeslot() + " • Seats: " + b.getSeat_required());

        // Toggle the delete button visibility based on delete mode
        if (isDeleteMode) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> listener.onClick(b)); // Delete on click
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.cv.setOnClickListener(v -> {
            if (!isDeleteMode) {
                // If not in delete mode, allow editing
                listener.onClick(b);
            }
        });
    }

    @Override public int getItemCount() { return bookings == null ? 0 : bookings.size(); }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_sub;
        CardView cv;
        Button deleteButton;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_booking_title);
            tv_sub = itemView.findViewById(R.id.tv_booking_sub);
            cv = itemView.findViewById(R.id.cv_booking);
            deleteButton = itemView.findViewById(R.id.btn_delete_booking);
        }
    }
    private String getmyDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(new Date());
        return formattedDate;
    }
}
