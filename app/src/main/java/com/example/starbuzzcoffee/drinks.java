package com.example.starbuzzcoffee;

public class drinks {
    private String name;
    private String description;
    private int imageResourceId;

    public static final drinks[] drink ={
            new drinks("latte","a couple of espresso shots with steamed milk",R.drawable.latte),
            new drinks("cappuccino","espresso,hot milk, and a steamed milk foam",R.drawable.cappuccino),
            new drinks("filter","highest quality beans roasted and brewed fresh",R.drawable.filter)
    };

    private drinks(String name, String description,int imageResourceId){
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public String getDescription(){
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getName() {
        return name;
    }



    public String toString() {
        return this.name;
    }
}

