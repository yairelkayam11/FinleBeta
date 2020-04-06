package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.finalebeta.CreateEvent.t;
import static com.example.finalebeta.CreateEvent.t;
import static com.example.finalebeta.FBref.refEvnts;

import java.util.ArrayList;



public class OrderAct extends AppCompatActivity {

    Button btn;
    String n;
    ArrayAdapter adp;
    ListView lv;




    ArrayList<String> Ast = new ArrayList<String>();
    ArrayList<UserOrder> UO = new ArrayList<UserOrder>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        btn=(Button)findViewById(R.id.btn);
    //    lv = (ListView)findViewById(R.id.lv);
    //    lv.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    //    lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    /*    ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                Ast.clear();
                UO.clear();

                for (DataSnapshot data : ds.getChildren()) {
                   n = (String) data.child(t).child("arrDP").child("name").getValue();
                    Evnts dataTMP = data.getValue(Evnts.class);
                    UO = dataTMP.getArrUO();
                    Ast.add(n);
                }

                adp = new ArrayAdapter<>(OrderAct.this,R.layout.support_simple_spinner_dropdown_item, Ast);
                lv.setAdapter(adp);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        refEvnts.child("Evnts").addValueEventListener(vel); */
    }

    public void AddDiner (View view) {

        Intent t = new Intent(this,DinerOrderAct.class);
        startActivity(t);


    }
}
