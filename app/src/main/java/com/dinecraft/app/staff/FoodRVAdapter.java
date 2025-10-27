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

import java.util.List;

public class FoodRVAdapter extends RecyclerView.Adapter<FoodRVAdapter.FoodViewHolder> {
    private final Context context;
    private final List<FoodModel> foodList;

    public FoodRVAdapter(Context context, List<FoodModel> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_food, parent, false);
        return new FoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodModel food = foodList.get(position);
        holder.tv_food_name.setText(food.getName());
        holder.tv_food_category.setText(food.getCategory());
        holder.tv_food_price.setText(String.valueOf(food.getPrice()));

        holder.cv_food.setOnClickListener(v -> {
            Intent i = new Intent(context, StaffFoodDetailActivity.class);
            i.putExtra("id", food.getId());
            i.putExtra("name", food.getName());
            i.putExtra("category", food.getCategory());
            i.putExtra("price", food.getPrice());
            i.putExtra("description", food.getDescription());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return foodList == null ? 0 : foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_food_name;
        public TextView tv_food_category;
        public TextView tv_food_price;
        public CardView cv_food;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_category = itemView.findViewById(R.id.tv_food_category);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);
            cv_food = itemView.findViewById(R.id.cv_food);
        }
    }
}
