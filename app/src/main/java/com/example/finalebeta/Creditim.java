package com.example.finalebeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import static com.example.finalebeta.FBref.refAuth;

public class Creditim extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditim);

    }

    public boolean onCreateOptionsMenu (Menu menu) {

        menu.add("Open Events");
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


        return true;
    }

}
