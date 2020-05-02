package com.example.finalebeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    ProgressBar pb;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/**
 *
 * when the pb finish after 3 seconds there is pass to login sign
 */

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent t = new Intent(MainActivity.this,SignInACT.class);
                startActivity(t);
                finish();
            }
        },SPLASH_TIME_OUT);


        prog();

    }

    /**
     *
     * this progressbar method, the bar charged from 0-100 for 3 seconds
     */

    public void prog() {

        pb = (ProgressBar) findViewById(R.id.pb);

        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {

                counter++;
                pb.setProgress(counter);

                if (counter==100)
                    t.cancel();

            }
        };
        t.schedule(tt,0,30);
    }


}
