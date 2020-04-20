package com.example.finalebeta;

import java.util.ArrayList;

public class UserOrder {

    ArrayList<DishPrice> ArrDP = new ArrayList<DishPrice>();
    private String name;
    private Double Totalprice;
    private Double Change;
    private Double MoneyPEID;
    private String Useruid;
    private String Feedback;
    private int Rate;
    boolean storage;

    public UserOrder(String name, DishPrice ArrDP){}

    public UserOrder(){}

    public UserOrder(String name, ArrayList<DishPrice> ArrDP, Double Totalprice , Double Change,Double MoneyPEID,String Useruid,String Feedback,int Rate , boolean storage) {

        this.name = name;
        this.ArrDP = ArrDP;
        this.Totalprice = Totalprice;
        this.Change = Change;
        this.MoneyPEID = MoneyPEID;
        this.Useruid = Useruid;
        this.Feedback = Feedback;
        this.Rate = Rate;
        this.storage = storage;

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

    public Double getTotalprice() {
        return Totalprice;
    }

    public void setTotalprice(Double totalprice) {
        Totalprice = totalprice;
    }

    public Double getChange() {
        return Change;
    }

    public void setChange(Double change) {
        Change = change;
    }

    public Double getMoneyPEID() {
        return MoneyPEID;
    }

    public void setMoneyPEID(Double moneyPEID) {
        MoneyPEID = moneyPEID;
    }

    public String getUseruid() {
        return Useruid;
    }

    public void setUseruid(String useruid) {
        Useruid = useruid;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public boolean isStorage() {
        return storage;
    }

    public void setStorage(boolean storage) {
        this.storage = storage;
    }
}


