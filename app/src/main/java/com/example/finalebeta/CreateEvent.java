package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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
import java.util.Calendar;
import java.util.Date;

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
    Long ID;
    String t;
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


    public void CreateEV (View view) {

        place = ETplace.getText().toString();
        Epass = ETepass.getText().toString();
        name = ETname.getText().toString();
        date = TVD.getText().toString();
        time = TVT.getText().toString();
        ID = count;
        t = ID.toString();



        evnt = new Evnts(ID,place,name,date,time,Epass,Active);
        refEvnts.child(t).setValue(evnt);
    }




}
