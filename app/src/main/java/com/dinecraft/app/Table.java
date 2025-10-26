package com.dinecraft.app;

import androidx.annotation.NonNull;

public class Table {
    private String id;
    private String name;
    private int seat;

    public Table(){}
    public Table(String id, String name, int seat) {
        this.id = id;
        this.name = name;
        this.seat = seat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    @NonNull
    public String toString(){
        return this.name + "(" + this.seat+")";
    }
}
