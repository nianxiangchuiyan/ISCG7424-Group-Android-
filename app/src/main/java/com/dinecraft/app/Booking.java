package com.dinecraft.app;

import java.util.Date;

public class Booking {
    private String booking_id;
    private Date date;
    private int timeslot;
    private int seat_required;
    private Table table;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getTable_name() {
        if(this.table == null) return "";
        return this.table.getName();
    }

//    public void setTable_name(String table_name) {
//        this.table.setName(table_name);
//    }

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
