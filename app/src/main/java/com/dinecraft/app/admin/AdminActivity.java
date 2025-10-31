package com.dinecraft.app.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dinecraft.app.BaseActivity;
import com.dinecraft.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Admin Activity to manage users.
 *
 * NOTE (Important):
 * - This implementation manages user *documents* inside Firestore collection "users".
 * - It does NOT delete or create Firebase Authentication accounts (that requires Admin SDK or Cloud Functions).
 * - To "create user", we create a Firestore user document and send a password-reset email to the email address so the user can set a password.
 * - To "reset password" for a user, we send a password reset email using FirebaseAuth.sendPasswordResetEmail.
 *
 * Put this file under: com/dinecraft/app/admin/AdminActivity.java
 */
public class AdminActivity extends BaseActivity {

    private RecyclerView rvUsers;
    private AdminUserAdapter adapter;
    private List<UserItem> userList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setTitle("Admin - User Management");
        setupBottomNav();
        setupTopProfile();

        rvUsers = findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminUserAdapter(userList, this::onUserAction);
        rvUsers.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        findViewById(R.id.btnAddUser).setOnClickListener(v -> showAddUserDialog());

        loadUsers();
    }

    private void loadUsers() {
        // Assumes a "users" collection where each doc has: uid (optional), name, email, role
        db.collection("users")
                .orderBy("email", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        showToast("Failed to load users: " + e.getMessage());
                        return;
                    }
                    userList.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot ds : snapshots.getDocuments()) {
                            String id = ds.getId();
                            String name = ds.getString("name");
                            String email = ds.getString("email");
                            String role = ds.getString("role");
                            userList.add(new UserItem(id, name, email, role));
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    // Callback from adapter when action on a user selected
    private void onUserAction(UserItem user, String action) {
        switch (action) {
            case "edit":
                showEditRoleDialog(user);
                break;
            case "delete":
                confirmAndDeleteUser(user);
                break;
            case "reset":
                sendPasswordReset(user);
                break;
        }
    }

    private void showEditRoleDialog(UserItem user) {
        final String[] roles = new String[]{"customer", "staff","administrator"};
        int checked = user.role != null && user.role.equals("administrator") ? 1 : 0;

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Change role for " + (user.email == null ? "" : user.email));
        b.setSingleChoiceItems(roles, checked, null);
        b.setPositiveButton("Save", (dialog, which) -> {
            ListView lw = ((AlertDialog) dialog).getListView();
            int sel = lw.getCheckedItemPosition();
            String newRole = roles[sel];
            db.collection("users").document(user.id)
                    .update("role", newRole)
                    .addOnSuccessListener(aVoid -> showToast("Role updated"))
                    .addOnFailureListener(ex -> showToast("Failed: " + ex.getMessage()));
        });
        b.setNegativeButton("Cancel", null);
        b.show();
    }

    private void confirmAndDeleteUser(UserItem user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete user")
                .setMessage("Delete Firestore record for " + user.email + "? ")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.collection("users").document(user.id)
                            .delete()
                            .addOnSuccessListener(aVoid -> showToast("User document deleted"))
                            .addOnFailureListener(ex -> showToast("Failed: " + ex.getMessage()));
                })
                .setNegativeButton("Cancel", null)
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void sendPasswordReset(UserItem user) {
        if (TextUtils.isEmpty(user.email)) {
            showToast("User has no email.");
            return;
        }
        auth.sendPasswordResetEmail(user.email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Password reset email sent to " + user.email);
                    } else {
                        showToast("Failed to send reset: " + (task.getException() != null ? task.getException().getMessage() : "unknown"));
                    }
                });
    }

    private void showAddUserDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_add_user, null);
        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etEmail = dialogView.findViewById(R.id.etEmail);
        Spinner spRole = dialogView.findViewById(R.id.spRole);

        ArrayAdapter<String> adapterRole = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"customer", "staff", "administrator"});
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapterRole);

        new AlertDialog.Builder(this)
                .setTitle("Add user (creates Firestore record and sends reset email)")
                .setView(dialogView)
                .setPositiveButton("Create", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String role = (String) spRole.getSelectedItem();

                    if (TextUtils.isEmpty(email)) {
                        showToast("Email required");
                        return;
                    }
                    // Create Firestore document
                    CollectionReference usersRef = db.collection("users");
                    // Use email as ID-safe key? we'll auto-generate
                    usersRef.add(new UserFirestore(name, email, role))
                            .addOnSuccessListener(documentReference -> {
                                showToast("User document created");
                                // send password reset email so the user can set their password
                                auth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                showToast("Reset email sent to " + email);
                                            } else {
                                                showToast("Failed to send reset email");
                                            }
                                        });
                            })
                            .addOnFailureListener(e -> showToast("Failed to create user doc: " + e.getMessage()));
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // simple helper showToast using BaseActivity method if exists or fallback
    private void showToast(String text) {
        try {
            // try BaseActivity utility
            java.lang.reflect.Method m = BaseActivity.class.getDeclaredMethod("showToast", String.class);
            m.setAccessible(true);
            m.invoke(this, text);
        } catch (Exception ex) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    // Simple POJOs used for Firestore
    public static class UserFirestore {
        public String name;
        public String email;
        public String role;

        public UserFirestore() {}

        public UserFirestore(String name, String email, String role) {
            this.name = name;
            this.email = email;
            this.role = role;
        }
    }

    public static class UserItem {
        public String id;
        public String name;
        public String email;
        public String role;

        public UserItem() {}

        public UserItem(String id, String name, String email, String role) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
        }
    }
}
