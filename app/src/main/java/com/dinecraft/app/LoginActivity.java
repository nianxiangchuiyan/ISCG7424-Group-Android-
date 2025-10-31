package com.dinecraft.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dinecraft.app.admin.AdminMainActivity;
import com.dinecraft.app.staff.StaffMainActivity;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore db;

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister, btnGoogleSignIn;

    private final ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle());
        btnLogin.setOnClickListener(v -> loginWithEmail());
        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void loginWithEmail() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            loadUserName(user.getUid());
                        }
                        Toast.makeText(this, "Great, redirecting to main page.", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(this, MainActivity.class));
//                        finish();
                    } else {
                        Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadUserName(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String name = snapshot.getString("name");
                        String role = snapshot.getString("role");
                        if (name != null) {
                            updateUserDisplayName(name);
                            Config.getInstance().setCurrUserRole(role);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Read failed", e));
    }
    private void updateUserDisplayName(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        UserProfileChangeRequest request =
                new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();

        user.updateProfile(request)
                .addOnSuccessListener(aVoid -> {
                    //Log.d("Auth", "DisplayName updated");
                    //Toast.makeText(this, "Load DisplayName successful", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                })
                .addOnFailureListener(e -> Log.e("Auth", "Update failed", e));

        user.reload();
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully
            Toast.makeText(this, "Google Sign-in Success!", Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            Log.e("GOOGLE_SIGN_IN", "Sign-in failed: code=" + e.getStatusCode() + ", message=" + e.getMessage(), e);
            Toast.makeText(this, "Sign-in failed: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        checkOrCreateFirestoreUser(user);
                    } else {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkOrCreateFirestoreUser(FirebaseUser user) {
        if (user == null) return;

        db.collection("users").document(user.getUid())
                .get()
                .addOnSuccessListener(document -> {
                    if (!document.exists()) {
                        // Create new Firestore entry for first-time Google login
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("email", user.getEmail());
                        newUser.put("name", user.getDisplayName());
                        newUser.put("role", "customer");

                        db.collection("users").document(user.getUid())
                                .set(newUser)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()
                                );
                    }

                    // Go to main screen regardless (existing or new)
                    Toast.makeText(this, "Welcome, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error checking user: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    public void loginAsAdmin(View view) {
        etEmail.setText("a@a.com");
        etPassword.setText("123456");
    }

    public void loginAsStaff(View view) {
        etEmail.setText("staff@a.com");
        etPassword.setText("123456");
    }
}
