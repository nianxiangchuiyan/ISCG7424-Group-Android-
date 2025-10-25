package com.dinecraft.app.staff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dinecraft.app.R;
import java.util.List;
public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FoodModel> foodList;
    private static final int HEADER_TYPE = 0;
    private static final int ITEM_TYPE = 1;

    public FoodAdapter(List<FoodModel> foodList) {
        this.foodList = foodList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_TYPE;  // First item is the header
        } else {
            return ITEM_TYPE;    // All others are food items
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_header_item, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
            return new FoodViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            // Set header data
            ((HeaderViewHolder) holder).bindHeader();
        } else {
            // Set food item data
            FoodModel food = foodList.get(position - 1); // Subtract 1 to skip the header
            ((FoodViewHolder) holder).bindData(food);
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size() + 1;  // Plus 1 for the header row
    }

    // ViewHolder for header
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvName, tvCategory, tvPrice, tvDescription;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }

        public void bindHeader() {
            tvId.setText("ID");
            tvName.setText("Name");
            tvCategory.setText("Category");
            tvPrice.setText("Price");
            tvDescription.setText("Description");
        }
    }

    // ViewHolder for food items
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvName, tvCategory, tvPrice, tvDescription;

        public FoodViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }

        public void bindData(FoodModel food) {
            tvId.setText(food.getId());
            tvName.setText(food.getName());
            tvCategory.setText(food.getCategory());
            tvPrice.setText(String.valueOf(food.getPrice()));
            tvDescription.setText(food.getDescription());
        }
    }
}
