package com.example.applicationtofragment;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    private String name;
    private String price;
    private String imageResource;

    public Food(String name, String price,String imageResource) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
    }
    public Food(Parcel in) {
        this.name = in.readString();
        this.price = in.readString();
        this.imageResource = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
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
        parcel.writeString(imageResource);
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {

        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
