import java.sql.Time;
import java.util.Date;

public class Event{


    private String ID;
    private String place;
    private Date date;
    private Time time;
    private String Epass;//event password
    private boolean Active;

    public Event() {
    }

    public Event(String ID, String place, Date date, Time time, String Epass) {
        this(ID, place, date, time, Epass, );
    }

    public Event(String ID, String place, Date date, Time time, String Epass , boolean Active) {

        this.Active = Active;
        this.date = date;
        this.ID = ID;
        this.time = time;
        this.place = place;
        this.Epass = Epass;

        Active = true;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public String getEpass() {
        return Epass;
    }

    public void setEpass(String epass) {
        Epass = epass;
    }
}
