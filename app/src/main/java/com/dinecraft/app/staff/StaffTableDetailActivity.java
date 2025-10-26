package com.dinecraft.app.staff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.Config;
import com.dinecraft.app.R;
import com.dinecraft.app.Table;
import com.google.firebase.firestore.FirebaseFirestore;

public class StaffTableDetailActivity extends BaseActivity {
    EditText et_name, et_seats;
    Button btn_ok, btn_cancel, btn_delete;
    Table table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_table_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initialize the bottom navigation bar
        setupBottomNav();
        setupStaffNav();

        et_name = findViewById(R.id.et_name);
        et_seats = findViewById(R.id.et_seats);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_delete = findViewById(R.id.btn_delete);

        table = Config.getInstance().getTable();
        String table_name = table.getName();
        String table_seats = String.valueOf(table.getSeat());
        et_name.setText(table_name);
        et_seats.setText(table_seats);
        btn_ok.setOnClickListener(v -> {
            table.setName(et_name.getText().toString());
            table.setSeat(Integer.parseInt(et_seats.getText().toString()));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("tables").document(table.getId()).set(table)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(StaffTableDetailActivity.this, "Table updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), StaffTableListActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(StaffTableDetailActivity.this, "Table update failed", Toast.LENGTH_SHORT).show();
                    });
        });
        btn_cancel.setOnClickListener(v ->{
            startActivity(new Intent(getApplicationContext(), StaffTableListActivity.class));
            finish();
        });
        btn_delete.setOnClickListener(v ->{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("tables").document(table.getId()).delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(StaffTableDetailActivity.this, "Table deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), StaffTableListActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(StaffTableDetailActivity.this, "Table delete failed", Toast.LENGTH_SHORT).show();
                    });

        });

    }
}