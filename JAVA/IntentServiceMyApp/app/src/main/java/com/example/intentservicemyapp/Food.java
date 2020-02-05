package com.example.intentservicemyapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    private String name;
    private String price;
    private String category;
    private String imageResource;

    public Food(String name, String price, String category,String imageResource) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageResource = imageResource;
    }
    public Food(Parcel in) {
        this.name = in.readString();
        this.price = in.readString();
        this.category = in.readString();
        this.imageResource = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
    public String getImageResource(){
        return imageResource;
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

    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {

        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
