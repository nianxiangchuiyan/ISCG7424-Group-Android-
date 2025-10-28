package com.dinecraft.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etName;
    private Button btnCreate;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        btnCreate = findViewById(R.id.btnCreate);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Register button click
        btnCreate.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();


        // Input validation
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create account in Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = auth.getCurrentUser().getUid();

                        // Store user info in Firestore
                        Map<String, Object> user = new HashMap<>();
                        user.put("email", email);
                        user.put("name", name);
                        user.put("role", "customer"); // Default role

                        db.collection("users").document(uid)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
