package com.dinecraft.app.staff;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dinecraft.app.Booking;
import com.dinecraft.app.Config;
import com.dinecraft.app.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class BookingRVAdapter extends RecyclerView.Adapter<BookingRVAdapter.BookingVH> {
    public List<Booking> bookings;
    public Context context;

    public BookingRVAdapter(List<Booking> bookings, Context context) {
        this.bookings = bookings;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_staff_booking, parent, false);
        return new BookingVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingVH holder, int position) {
        Booking booking = bookings.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        holder.tv_date.setText(sdf.format(booking.getDate()));
        holder.tv_contactnumber.setText(booking.getContact_number());
        holder.tv_timeslot.setText(String.valueOf(booking.getTimeslot()));
        holder.tv_memo.setText(booking.getMemo());
        holder.tv_tablename.setText(booking.getTable_name());
        holder.tv_prefname.setText(booking.getPref_name());
        holder.tv_seatrequired.setText(String.valueOf(booking.getSeat_required()));
        holder.cardview.setOnClickListener( aVoid -> {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            Config.getInstance().setStaffBooking(booking);
            context.startActivity(new Intent(context, StaffBookingDetailActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static  class BookingVH extends RecyclerView.ViewHolder {
        public CardView cardview;
        public TextView tv_date, tv_contactnumber, tv_memo, tv_prefname, tv_seatrequired, tv_tablename, tv_timeslot;

        public BookingVH(@NonNull View itemView) {
            super(itemView);
            cardview = itemView.findViewById(R.id.cv_booking);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_contactnumber = itemView.findViewById(R.id.tv_contactnumber);
            tv_memo = itemView.findViewById(R.id.tv_memo);
            tv_prefname = itemView.findViewById(R.id.tv_prefername);
            tv_seatrequired = itemView.findViewById(R.id.tv_seatrequired);
            tv_tablename = itemView.findViewById(R.id.tv_tablename);
            tv_timeslot = itemView.findViewById(R.id.tv_timeslot);
        }
    }
}
