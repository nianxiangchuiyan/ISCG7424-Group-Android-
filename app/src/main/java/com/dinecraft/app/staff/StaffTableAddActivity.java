package com.dinecraft.app.staff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.Config;
import com.dinecraft.app.R;
import com.dinecraft.app.Table;
import com.google.firebase.firestore.FirebaseFirestore;

public class StaffTableAddActivity extends BaseActivity {
    EditText et_name, et_seats;
    Button btn_ok, btn_cancel;
    Table table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_table_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initialize the bottom navigation bar
        setupBottomNav();
        setupStaffNav();
        setupTopProfile();

        et_name = findViewById(R.id.et_name);
        et_seats = findViewById(R.id.et_seats);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);

        table = new Table();

        btn_ok.setOnClickListener(v -> {
            table.setId("2");
            table.setName(et_name.getText().toString());
            table.setSeat(Integer.parseInt(et_seats.getText().toString()));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("tables")
                    .add(table)
                    .addOnSuccessListener(documentReference -> {
                        documentReference.update("id", documentReference.getId());
                        Config.getInstance().getTableList().add(table);
                        Toast.makeText(StaffTableAddActivity.this, "Table added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), StaffTableListActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(StaffTableAddActivity.this, "Table add failed", Toast.LENGTH_SHORT).show();
                    });
        });
        btn_cancel.setOnClickListener(v ->{
            startActivity(new Intent(getApplicationContext(), StaffTableListActivity.class));
            finish();
        });

    }
}