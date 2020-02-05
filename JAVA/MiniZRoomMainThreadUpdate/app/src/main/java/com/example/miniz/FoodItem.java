package com.example.miniz;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_table")
public class FoodItem{

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String name;
    private String price;
    private String imageResource;
    private String category;

    public String getName(){
        return name;
    }
    public String getPrice(){
        return price;
    }
    public String getImageResource(){
        return imageResource;
    }
    public String getCategory() {return category;}

    public FoodItem(@NonNull String name, @NonNull String price, @NonNull String category, @NonNull String imageResource){
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.category = category;
    }

    /*
    public FoodItem(Parcel parcel){
        this.name = parcel.readString();
        this.price = parcel.readString();
        this.category = parcel.readString();
        this.imageResource = parcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(category);
        parcel.writeString(imageResource);
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel parcel) {
            return new FoodItem(parcel);
        }

        @Override
        public FoodItem[] newArray(int i) {
            return new FoodItem[0];
        }
    };
    */
}
