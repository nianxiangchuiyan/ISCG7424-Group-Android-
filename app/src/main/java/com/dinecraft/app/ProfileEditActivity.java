package com.dinecraft.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileEditActivity extends BaseActivity {
    EditText etName;
    Button btnOK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the bottom navigation bar
        setupBottomNav();
        setupTopProfile();

        etName = findViewById(R.id.et_name);
        btnOK = findViewById(R.id.btnOK);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser curUser = mAuth.getCurrentUser();
        if(etName!= null && curUser != null)  etName.setText(curUser.getDisplayName());

        btnOK.setOnClickListener(v -> {
            try {
                String name = etName.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(curUser.getUid())
                        .update("name", name)
                        .addOnSuccessListener(aVoid -> {
                            UserProfileChangeRequest request =
                                    new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build();

                            curUser.updateProfile(request)
                                    .addOnSuccessListener(a -> {
                                        Log.d("Auth", "DisplayName updated");
                                        Toast.makeText(this, "Update DisplayName successful", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> Log.e("Auth", "Update failed", e));

                        });


            } catch (Exception e) {
                Log.e("Auth", "Update failed", e);
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}