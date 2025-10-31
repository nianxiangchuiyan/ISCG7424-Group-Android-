package com.dinecraft.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking {
    private String booking_id;
    private String date;
    private int timeslot;
    private int seat_required;
    private String table_name;
    private String pref_name;
    private String contact_number;
    private String memo;

    public Booking(){}

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String dateString) {
        Date date = null;
        // 1. Define the format of the input string
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        // 2. Define the desired output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // 3. Parse the string into a Date object
            date = inputFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parsing error, e.g., by logging or showing a message to the user
        }

        if (date != null) {
            // 4. Format the Date object into the desired string format
            String formattedDate = outputFormat.format(date);
            this.date=formattedDate;
        }
    }

    public int getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    public int getSeat_required() {
        return seat_required;
    }

    public void setSeat_required(int seat_required) {
        this.seat_required = seat_required;
    }

    public String getTable_name() {
        return this.table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getPref_name() {
        return pref_name;
    }

    public void setPref_name(String pref_name) {
        this.pref_name = pref_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
