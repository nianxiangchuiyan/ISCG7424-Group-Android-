package com.dinecraft.app.staff;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dinecraft.app.R;
import com.dinecraft.app.Table;

import java.util.List;

public class TableRVAdapter extends RecyclerView.Adapter<TableRVAdapter.TableViewHolder> {
    private List<Table> tableList;
    Context context;
    public TableRVAdapter(Context context, List<Table> tableList){
        this.tableList = tableList;
        this.context = context;
    }

    public TableRVAdapter() {}

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_table, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        Table table = tableList.get(position);
        holder.tv_table_name.setText(table.getName());
        holder.tv_table_seat.setText(String.valueOf(table.getSeat()));
        holder.cv_table.setOnClickListener( view -> {
            //TODO put parameters
            Intent i = new Intent(context, StaffTableDetailActivity.class);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public class TableViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_table_name;
        public TextView tv_table_seat;
        public CardView cv_table;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_table_name = itemView.findViewById(R.id.tv_table_name);
            tv_table_seat = itemView.findViewById(R.id.tv_table_seat);
            cv_table = itemView.findViewById(R.id.cv_table);
        }
    }
}
