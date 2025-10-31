package com.dinecraft.app.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dinecraft.app.R;

import java.util.List;

/**
 * RecyclerView adapter for admin user list.
 * Put under com/dinecraft/app/admin/AdminUserAdapter.java
 */
public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.VH> {

    public interface ActionCallback {
        void onAction(AdminActivity.UserItem user, String action);
    }

    private List<AdminActivity.UserItem> list;
    private ActionCallback callback;

    public AdminUserAdapter(List<AdminActivity.UserItem> list, ActionCallback cb) {
        this.list = list;
        this.callback = cb;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_user, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        AdminActivity.UserItem u = list.get(position);
        holder.tvName.setText(u.name != null ? u.name : "(no name)");
        holder.tvEmail.setText(u.email != null ? u.email : "(no email)");
        holder.tvRole.setText(u.role != null ? u.role : "customer");

        holder.btnEdit.setOnClickListener(v -> callback.onAction(u, "edit"));
        holder.btnDelete.setOnClickListener(v -> callback.onAction(u, "delete"));
        holder.btnReset.setOnClickListener(v -> callback.onAction(u, "reset"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvRole;
        Button btnEdit, btnDelete, btnReset;
        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRole = itemView.findViewById(R.id.tvRole);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnReset = itemView.findViewById(R.id.btnReset);
        }
    }
}
