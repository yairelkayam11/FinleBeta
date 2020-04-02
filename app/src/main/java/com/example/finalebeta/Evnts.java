package com.example.finalebeta;

public class Evnts {

    private Long ID;
    private String place;
    private String name;
    private String date;
    private String time;
    private String Epass;//event password
    private boolean Active;

    public Evnts(){}
    public Evnts(Long ID, String place,String name , String date, String time, String Epass , boolean Active ) {
        this.ID = ID;
        this.time = time;
        this.name = name;
        this.date = date;
        this.place = place;
        this.Active = Active;
        this.Epass = Epass;
    }

    public Long getID() {
            return ID;
        }

    public void setID(Long ID) {
            this.ID = ID;
        }

    public String getPlace() {
            return place;
    }

    public void setPlace(String place) {
            this.place = place;
        }

    public String getDate() {
            return date;
        }

    public void setDate(String date) {
            this.date = date;
        }

    public String getTime() {
            return time;
        }

    public void setTime(String time) {
            this.time = time;
        }

    public String getEpass() {
            return Epass;
        }

    public void setEpass(String epass) {
            Epass = epass;
        }

    public String getName() { return name;
    }

    public void setName(String name) { this.name = name;
    }

    public boolean isActive() { return Active;
    }

    public void setActive(boolean active) { Active = active;
    }
}

