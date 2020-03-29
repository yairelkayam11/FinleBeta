package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import static com.example.finalebeta.FBref.refAuth;
import static com.example.finalebeta.FBref.refUsers;

public class SignUpACT extends AppCompatActivity {

    String name, email, uid;
    TextView tVnameview, tVemailview, tVuidview;
    CheckBox cBconnectview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_a_c_t);

        tVnameview=(TextView)findViewById(R.id.tVnameview);
        tVemailview=(TextView)findViewById(R.id.tVemailview);
        tVuidview=(TextView)findViewById(R.id.tVuidview);
        cBconnectview=(CheckBox)findViewById(R.id.cBconnectview);


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = refAuth.getCurrentUser();
//        name = user.getDisplayName();
//        tVnameview.setText(name);
        email = user.getEmail();
        tVemailview.setText(email);
        uid = user.getUid();
        tVuidview.setText(uid);
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        cBconnectview.setChecked(isChecked);
    }
    public void update(View view) {
        FirebaseUser user = refAuth.getCurrentUser();
        if (!cBconnectview.isChecked()){
            refAuth.signOut();
        }
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putBoolean("stayConnect",cBconnectview.isChecked());
        editor.commit();
        finish();
    }
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menuLogin) {
            Intent si = new Intent(SignUpACT.this,SignUpACT.class);
            startActivity(si);
        }
        if (id==R.id.menuDB) {

        }
        return true;
    }




                }

