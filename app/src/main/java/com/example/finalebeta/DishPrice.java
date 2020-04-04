package com.example.finalebeta;

public class DishPrice {

    private String dish;
    private float price;

    public DishPrice(){}

    public DishPrice (String dish , float price){

        this.dish = dish;
        this.price = price;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


}
