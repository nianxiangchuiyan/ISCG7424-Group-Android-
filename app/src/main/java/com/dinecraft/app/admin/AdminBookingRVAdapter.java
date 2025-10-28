package com.dinecraft.app.admin;

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
import java.util.List;
import java.util.Locale;

public class AdminBookingRVAdapter extends RecyclerView.Adapter<AdminBookingRVAdapter.BookingViewHolder> {

    public interface OnBookingClick {
        void onClick(Booking booking);
    }

    private final Context context;
    private final List<Booking> bookings;
    private final OnBookingClick listener;
    private final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public AdminBookingRVAdapter(Context context, List<Booking> bookings, OnBookingClick listener) {
        this.context = context;
        this.bookings = bookings;
        this.listener = listener;
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
        String dateStr = b.getDate() != null ? fmt.format(b.getDate()) : "";
        holder.tv_sub.setText("Date: " + dateStr + " • Slot: " + b.getTimeslot() + " • Seats: " + b.getSeat_required());

        holder.cv.setOnClickListener(v -> listener.onClick(b));  // Open booking details
    }

    @Override public int getItemCount() { return bookings == null ? 0 : bookings.size(); }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_sub;
        CardView cv;
        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_booking_title);
            tv_sub = itemView.findViewById(R.id.tv_booking_sub);
            cv = itemView.findViewById(R.id.cv_booking);
        }
    }
}
