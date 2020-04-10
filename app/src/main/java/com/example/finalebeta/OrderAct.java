package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
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



public class OrderAct extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button btn;
    ArrayAdapter adp;
    ListView lv;
    String nameTMP;
    ArrayList<DishPrice> ArrTMP;
    int i = 0;
    UserOrder userorder;




    ArrayList<String> Ast = new ArrayList<String>();
    ArrayList<UserOrder> UO = new ArrayList<UserOrder>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        btn=(Button)findViewById(R.id.btn);
        lv = (ListView)findViewById(R.id.lv2);
        lv.setOnItemClickListener (OrderAct.this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                Ast.clear();
                UO.clear();

                for (DataSnapshot data : ds.getChildren()) {
                    Evnts dataTMP = data.getValue(Evnts.class);


                    while(i<dataTMP.getArrUO().size()) {
                        userorder = new UserOrder(dataTMP.getArrUO().get(i).getName(), dataTMP.getArrUO().get(i).getArrDP());
                        UO.add(userorder);
                        Ast.add((dataTMP.getArrUO().get(i).getName()));
                        i++;
                    }


                }

                adp = new ArrayAdapter<>(OrderAct.this,R.layout.support_simple_spinner_dropdown_item, Ast);
                lv.setAdapter(adp);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        refEvnts.child("Evnts").addValueEventListener(vel);
    }

    public void AddDiner (View view) {

        Intent t = new Intent(this,DinerOrderAct.class);
        startActivity(t);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
