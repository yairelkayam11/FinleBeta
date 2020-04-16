package com.example.finalebeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent t = new Intent(this,SignInACT.class); // מסך ראשוני שמטרתו לעשות מעבר של מסך בפתיחת האפליקציה
        startActivity(t);


    }

}
