package com.dinecraft.app;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;



import java.util.ArrayList;
import java.util.List;

public class Config {
    private static Config instance;
    private Config() {}

    private Table table;

    private List<Table> listTable;

    public static synchronized Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public static void init_spinner(Spinner target_spn, int arrayID, Context context){
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
    public void init_table_spinner(Spinner spn_target, Context context){
        //spn_category = findViewById(R.id.spn_createquiz_category);

        if(listTable == null) {
            listTable = new ArrayList<>();
            listTable.add(new Table("1", "Table 1", 4));
            listTable.add(new Table("2", "Table 2", 4));
            //return;
        }

        ArrayAdapter<Table> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                listTable
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_target.setAdapter(adapter);
    }


    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<Table> getTableList() {
        return listTable;
    }

    public void setTableList(List<Table> listTable) {
        this.listTable = listTable;
    }
}
