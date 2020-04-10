package com.example.finalebeta;

import java.util.ArrayList;

public class UserOrder {

    ArrayList<DishPrice> ArrDP = new ArrayList<DishPrice>();
    private String name;

    public UserOrder(String name, DishPrice ArrDP){}

    public UserOrder(){}

    public UserOrder(String name, ArrayList<DishPrice> ArrDP) {

        this.name = name;
        this.ArrDP = ArrDP;

    }

    public ArrayList<DishPrice> getArrDP() {
        return ArrDP;
    }

    public void setArrDP(ArrayList<DishPrice> arrDP) {
        ArrDP = arrDP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


