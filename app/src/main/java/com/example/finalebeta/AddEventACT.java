package com.example.finalebeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddEventACT extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_a_c_t);

        btn=(Button)findViewById(R.id.btn);
    }

    public void AddEvent (View view) {

        Intent t = new Intent(this,CreateEvent.class);
        startActivity(t);


    }
}
