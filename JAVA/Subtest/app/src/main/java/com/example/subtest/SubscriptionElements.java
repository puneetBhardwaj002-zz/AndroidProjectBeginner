package com.example.subtest;

public class SubscriptionElements {
    private String name;
    private String id;
    private String amount;
    private String date;
    private String imageResource;

    public SubscriptionElements(String name, String id, String amount, String date, String imageResource){
        this.name = name;
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.imageResource = imageResource;
        setMerchant(name);
        setSubID(id);
        setNextPaymentAmount(amount);
        setNextPaymentDate(date);
        setImageResource(imageResource);
    }

    public String getMerchant() {
        return name;
    }

    public void setMerchant(String name) {
        this.name = name;
    }

    public String getSubID() {
        return id;
    }

    public void setSubID(String id) {
        this.id = id;
    }

    public String getNextPaymentAmount() {
        return amount;
    }

    public void setNextPaymentAmount(String status) {
        this.amount = status;
    }

    public String getNextPaymentDate() {
        return date;
    }

    public void setNextPaymentDate(String date) {
        this.date = date;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}
