package com.dinecraft.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<Food> foodList;

    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_list_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);

        holder.tvName.setText(food.getName());
        holder.tvDesc.setText(food.getDescription());
        holder.tvPrice.setText("$" + food.getPrice());

        // ✅ Get Firestore imageUrl safely
        String imageName = food.getImageUrl();
        int resId = 0;

        if (imageName != null && !imageName.trim().isEmpty()) {
            resId = holder.itemView.getContext().getResources()
                    .getIdentifier(imageName, "drawable",
                            holder.itemView.getContext().getPackageName());
        }

        if (resId == 0) {
            resId = R.drawable.placeholder; // Fallback
        }

        holder.imgFood.setImageResource(resId);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvPrice;
        ImageView imgFood;

        FoodViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvDesc = itemView.findViewById(R.id.tvFoodDesc);
            tvPrice = itemView.findViewById(R.id.tvFoodPrice);
            imgFood = itemView.findViewById(R.id.foodImage);
        }
    }
}
