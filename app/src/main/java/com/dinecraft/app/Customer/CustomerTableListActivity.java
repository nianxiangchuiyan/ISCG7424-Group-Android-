package com.dinecraft.app.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.Config;
import com.dinecraft.app.R;
import com.dinecraft.app.Table;
import com.dinecraft.app.staff.StaffTableAddActivity;
import com.dinecraft.app.staff.TableRVAdapter;

import java.util.List;

public class CustomerTableListActivity extends BaseActivity {
    private List<Table> tableList;
    RecyclerView rv;
    TableRVAdapter adapter;
    Button btn_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cus_table_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the bottom navigation bar
        setupBottomNav();
        setupCusNav();
        setupTopProfile();
        try{
        tableList = Config.getInstance().getTableList();}catch (Exception e){
            e.printStackTrace();
        }

        rv = findViewById(R.id.rv_cus_table_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TableRVAdapter(this, tableList);
        rv.setAdapter(adapter);

    }
}