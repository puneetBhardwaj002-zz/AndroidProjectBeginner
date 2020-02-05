package com.example.applicationtofragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    final ArrayList<Food> foodList;
    LayoutInflater inflater;
    Context context;


    public FoodAdapter(Context context, ArrayList<Food> foodList) {
        inflater = LayoutInflater.from(context);
        this.foodList = foodList;
        this.context = context;
    }


    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_food_item, parent, false);
        return new FoodViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.nameText.setText(food.getName());
        String price = context.getResources().getString(R.string.rupee_symbol)+food.getPrice();
        holder.priceText.setText(price);
        Picasso.with(context).load(foodList.get(position).getImageResource()).resize(100, 100).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(foodList == null)
            return 0;
        return foodList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final TextView nameText;
        public final TextView priceText;
        final FoodAdapter mAdapter;

        public FoodViewHolder(View itemView, FoodAdapter adapter) {
            super(itemView);
            imageView = itemView.findViewById(R.id.food_image);
            nameText = itemView.findViewById(R.id.food_name);
            priceText = itemView.findViewById(R.id.food_price);
            this.mAdapter = adapter;
        }
    }


}
