package com.example.myzomato2;

public class Food {
    private String nameText;
    private String priceText;
    private String categoryText;
    private String imageResources;

    public Food(String name, String price, String category, String imageId) {
        this.nameText = name;
        this.priceText = price;
        this.categoryText = category;
        this.imageResources = imageId;
    }

    public String getNameText() {
        return nameText;
    }

    public String getPriceText() {
        return priceText;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public String getImageResources() {
        return imageResources;
    }
}
