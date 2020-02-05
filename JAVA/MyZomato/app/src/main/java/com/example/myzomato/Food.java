package com.example.myzomato;

public class Food {
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
}
