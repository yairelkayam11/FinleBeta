package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.finalebeta.FBref.refEvnts;

public class AddEventACT extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<String> IDlist = new ArrayList<>();
    ArrayList<Evnts> Values = new ArrayList<>();


    Button btn;
    ListView lv;
    ArrayAdapter adp;
    Evnts dataa;
    ValueEventListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_a_c_t);

        btn=(Button)findViewById(R.id.btn);
        lv = (ListView)findViewById(R.id.lv);

         ValueEventListener listener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                IDlist.clear();
                Values.clear();

                for(DataSnapshot data : ds.getChildren()) {

                    Evnts dataTMP1 = data.getValue(Evnts.class);
                    IDlist.add(dataTMP1.getName());
                    dataa = dataTMP1;
                }

                adp = new ArrayAdapter<String>(AddEventACT.this,R.layout.support_simple_spinner_dropdown_item, IDlist);
                lv.setAdapter(adp);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
     };

        refEvnts.addValueEventListener(listener);
        lv.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


    }


    public void AddEvent (View view) {

        Intent t = new Intent(this,CreateEvent.class);
        startActivity(t);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle(dataa.getName());
        final EditText et = new EditText(this);
        et.setHint("Enter password :");
        adb.setView(et);
        adb.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = et.getText().toString();
                if (str !=dataa.getEpass()) {
                    Toast.makeText(AddEventACT.this , "Incorrect password ",Toast.LENGTH_SHORT).show();
                }
                else {

                    Toast.makeText(AddEventACT.this , "correct :) ",Toast.LENGTH_SHORT).show();

                    // מעבר לאקטיביטי של ההזמנה
                }
            }
        });
        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog ad = adb.create();
        ad.show();

    }
}
