package com.dinecraft.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private final List<Food> foodList;

    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_list_item, parent, false); // keep your xml name
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.tvName.setText(food.getName());
        holder.tvDesc.setText(food.getDescription());
        holder.tvPrice.setText("$" + String.format("%.2f", food.getPrice()));

        // load drawable by imageName (must match file in res/drawable without extension)
        Context ctx = holder.itemView.getContext();
        String imageName = food.getImageName();
        int resId = 0;
        if (imageName != null && !imageName.isEmpty()) {
            resId = ctx.getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        }

        if (resId != 0) {
            holder.foodImage.setImageResource(resId);
        } else {
            // fallback placeholder (use any drawable you have)
            holder.foodImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return foodList == null ? 0 : foodList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView tvName, tvDesc, tvPrice;

        FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage); // matches food_list_item.xml provided earlier
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvDesc = itemView.findViewById(R.id.tvFoodDesc);
            tvPrice = itemView.findViewById(R.id.tvFoodPrice);
        }
    }
}
