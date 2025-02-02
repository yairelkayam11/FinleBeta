package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Event;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.finalebeta.FBref.refAuth;
import static com.example.finalebeta.FBref.refEvnts;

public class CreateEvent extends AppCompatActivity {

    Button btn, TimeBtn, DateBtn;
    EditText ETplace, ETepass,ETname;
    TextView TVD, TVT;
    Evnts evnt;
    String place, Epass,time,date,name;
    DatePickerDialog dpd;
    TimePickerDialog tpd;
    Calendar c;
    Long count;
    Long IDD;
   public static Long t;
   boolean Active = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        btn = (Button) findViewById(R.id.btn);
        TVD = (TextView) findViewById(R.id.TVD);
        TVT = (TextView) findViewById(R.id.TVT);
        TimeBtn = (Button) findViewById(R.id.TimeBtn);
        ETplace = (EditText) findViewById(R.id.ETplace);
        DateBtn = (Button) findViewById(R.id.DateBtn);
        ETepass = (EditText) findViewById(R.id.ETepass);
        ETname = (EditText) findViewById(R.id.ETname);


        /**
         *
         *  Method for DatePicker that open Calendar
         *  <p>
         */


        DateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        Toast.makeText(CreateEvent.this, "" + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                        TVD.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, day, month, year);
                dpd.show();

            }
        });

        /**
         *
         *  Method for TimePicker that open Clock
         *  <p>
         */

        TimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);

                tpd = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        Toast.makeText(CreateEvent.this, "" + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
                        TVT.setText(hourOfDay + ":" + minute);
                    }

                } ,hour, min,false);
                tpd.show();
            }
        });

        /**
         *
         * this method read data from firebase and count the amount of childrens for make ID
         * <p>
         */

        ValueEventListener listener = new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot ds)
            {
                count = ds.getChildrenCount();
                count++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };
        refEvnts.addValueEventListener(listener);
    }

    /**
     *
     * saveing the event details
     * @param view
     * <p>
     */


    public void CreateEV (View view) {

        place = ETplace.getText().toString();
        if (place.isEmpty()) Toast.makeText(CreateEvent.this, "you must enter a place ", Toast.LENGTH_SHORT).show();
        Epass = ETepass.getText().toString();
        if (Epass.isEmpty()) Toast.makeText(CreateEvent.this, "you must enter a password ", Toast.LENGTH_SHORT).show();
        name = ETname.getText().toString();
        if (name.isEmpty()) Toast.makeText(CreateEvent.this, "you must enter event name ", Toast.LENGTH_SHORT).show();
        date = TVD.getText().toString();
        if (date.isEmpty()) Toast.makeText(CreateEvent.this, "you must enter a date ", Toast.LENGTH_SHORT).show();
        time = TVT.getText().toString();
        if (time.isEmpty()) Toast.makeText(CreateEvent.this, "you must enter a time ", Toast.LENGTH_SHORT).show();
        IDD = count;
        t = IDD;

        /**
         * pushing event data to firebase
         * <p>
         */


        if(!place.isEmpty()&&!Epass.isEmpty()&&!name.isEmpty()&&!date.isEmpty()&&!time.isEmpty()) {

            evnt = new Evnts(IDD, place, name, date, time, Epass, Active,null);
            refEvnts.child(""+t).setValue(evnt);

            Intent go = new Intent(this , AddEventACT.class);
            startActivity(go);

        }

    }

    public boolean onCreateOptionsMenu (Menu menu) {

        menu.add("Open Events");
        menu.add("Credits");
        menu.add("Logout");

        return true;

    }



    public boolean onOptionsItemSelected(MenuItem item){

        String str = item.getTitle().toString();

        if (str.equals("Open Events")) {

            Intent t = new Intent(this,AddEventACT.class);
            startActivity(t);
        }
        if (str.equals("Logout")) {


            refAuth.signOut();
            SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("stayConnect",false);
            editor.commit();
            Intent t = new Intent(this, SignInACT.class);
            startActivity(t);

        }
        if (str.equals("Credits")) {

            Intent t = new Intent(this,Creditim.class);
            startActivity(t);
        }

        return true;
    }








}
