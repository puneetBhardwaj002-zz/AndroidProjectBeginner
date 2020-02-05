package com.example.miniz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {

    private List<FoodItem> foodItems;
    private LayoutInflater layoutInflater;
    private Context context;

    FoodItemAdapter(Context context, List<FoodItem> foods) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.foodItems = foods;
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.single_food_item, parent, false);
        return new FoodItemViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        if (foodItems != null) {
            FoodItem foodItem = foodItems.get(position);
            holder.nameText.setText(foodItem.getName());
            String price = context.getResources().getString(R.string.rupee) + foodItem.getPrice();
            holder.priceText.setText(price);
            Glide.with(context).load(foodItem.getImageResource()).into(holder.imageView);
        }
    }

    void setFoods(List<FoodItem> foods, int num){
        if(num==0){
            for(int i=0;i<foods.size();i++){
                if(foods.get(i).getCategory().equals("non-veg")){
                    foods.remove(foods.get(i));
                    i--;
                }
            }
        }else if(num ==1){
            for(int i=0;i<foods.size();i++){
                if(foods.get(i).getCategory().equals("veg")){
                    foods.remove(foods.get(i));
                    i--;
                }
            }
        }
        foodItems = foods;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (foodItems == null) {
            return 0;
        }
        return foodItems.size();
    }

    class FoodItemViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameText;
        final TextView priceText;
        final FoodItemAdapter adapter;

        private FoodItemViewHolder(View itemView, FoodItemAdapter adapter) {
            super(itemView);
            imageView = itemView.findViewById(R.id.foodImage);
            nameText = itemView.findViewById(R.id.foodName);
            priceText = itemView.findViewById(R.id.foodPrice);
            this.adapter = adapter;
        }
    }
}
