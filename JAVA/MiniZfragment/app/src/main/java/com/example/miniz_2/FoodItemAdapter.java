package com.example.miniz_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {

    private final ArrayList<FoodItem> foodItems;
    private LayoutInflater layoutInflater;
    private Context context;

    public FoodItemAdapter(Context context, ArrayList<FoodItem> foods) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.foodItems = foods;
    }

    @Override
    public FoodItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.single_food_item, parent, false);
        return new FoodItemViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.nameText.setText(foodItem.getName());
        String price = context.getResources().getString(R.string.rupee) + foodItem.getPrice();
        holder.priceText.setText(price);
        Picasso.with(context).load(foodItem.getImageResource()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (foodItems == null) {
            return 0;
        } else {
            return foodItems.size();
        }
    }

    class FoodItemViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameText;
        final TextView priceText;
        final FoodItemAdapter adapter;

        public FoodItemViewHolder(View itemView, FoodItemAdapter adapter) {
            super(itemView);
            imageView = itemView.findViewById(R.id.foodImage);
            nameText = itemView.findViewById(R.id.foodName);
            priceText = itemView.findViewById(R.id.foodPrice);
            this.adapter = adapter;
        }
    }
}
