package com.dinecraft.app;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private static Config instance;

    private Config() {
    }

    //This member var is ONLY used by Staff's activities
    private Table table;

    private List<Table> listTable;

    public static synchronized Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public static void init_spinner(Spinner target_spn, int arrayID, Context context) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                arrayID,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        target_spn.setAdapter(adapter);
    }

    //
//    public static void init_filter_spinner(Spinner spn_quiz_filter, int arrayID, Context context){
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                context,
//                arrayID,
//                android.R.layout.simple_spinner_item
//        );
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spn_quiz_filter.setAdapter(adapter);
//    }
//
    public void init_table_spinner(Spinner spn_target, Context context) {
        //spn_category = findViewById(R.id.spn_createquiz_category);

        //if (listTable == null || listTableUpdated) {
            fetchTables(new FirestoreCallback() {
                @Override
                public void onComplete(List<Table> tables) {

                    ArrayAdapter<Table> adapter = new ArrayAdapter<>(
                            context,
                            android.R.layout.simple_spinner_item,
                            listTable
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_target.setAdapter(adapter);

                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(context, "Error fetching tables: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            //return;
        //}
    }


    //This method is ONLY used by Staff's activities
    public Table getTable() {
        return table;
    }

    //This method is ONLY used by Staff's activities
    public void setTable(Table table) {
        this.table = table;
    }

    //This method can be used by all activities, listTable is global.
    public List<Table> getTableList() {
        return listTable;
    }
    //This method can be used by all activities, listTable is global.
    public void setTableList(List<Table> listTable) {
        this.listTable = listTable;
    }

    //Define a interface para for fetchTable() to callback
    public interface FirestoreCallback {
        void onComplete(List<Table> tables);

        void onFailure(Exception e);
    }

    public void fetchTables(FirestoreCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tables")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Tables fetched successfully");
                        listTable = new ArrayList<>();
                        for (var doc : task.getResult()) {
                            Table t = doc.toObject(Table.class);
                            listTable.add(t);
                        }

                        if (callback != null) {
                            callback.onComplete(listTable);
                        }
                    } else {
                        if (callback != null) {
                            callback.onFailure(task.getException());
                        }

                    }
                })
                .addOnFailureListener(v -> {
                    listTable = null;
                    Log.d("Firebase", "Error getting Tables: ", v);
                    if(callback != null)  callback.onFailure(v);
                });

    }
}
