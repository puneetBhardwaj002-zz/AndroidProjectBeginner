package com.example.myzomato2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodCustomAdapter extends ArrayAdapter<Food> {
    private ArrayList<Food> foods;
    private Context context;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = convertView;
        FoodViewHolder holder=null;
        if(root == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            root = inflater.inflate(R.layout.single_food_item, parent, false);
            holder = new FoodViewHolder(root);
            root.setTag(holder);
        }else{
            holder = (FoodViewHolder) root.getTag();
        }
        Food foodItem = foods.get(position);
        holder.nameText.setText(foodItem.getNameText());
        holder.priceText.setText(foodItem.getPriceText());
        holder.categoryText.setText(foodItem.getCategoryText());
        if(foodItem.getCategoryText().equals("veg")) {
            holder.categoryText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else{
            holder.categoryText.setTextColor(context.getResources().getColor(R.color.Red));
        }
        Picasso.with(context).load(foodItem.getImageResources()).into(holder.imageView);
        //Log.d("Inside getView method",holder.nameText.getText().toString());
        return root;
    }

    FoodCustomAdapter(Context context, ArrayList<Food> objects) {
        super(context, R.layout.single_food_item, objects);
        this.context = context;
        this.foods = objects;
    }
}

class FoodViewHolder{
    CircleImageView imageView;
    TextView nameText;
    TextView priceText;
    TextView categoryText;

    FoodViewHolder(View view){
        imageView = view.findViewById(R.id.foodImage);
        nameText = view.findViewById(R.id.foodName);
        priceText = view.findViewById(R.id.foodPrice);
        categoryText = view.findViewById(R.id.foodCategory);
    }
}
