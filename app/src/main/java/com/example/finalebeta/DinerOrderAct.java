package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.finalebeta.CreateEvent.t;
import static com.example.finalebeta.FBref.refUsers;
import static com.example.finalebeta.FBref.refEvnts;


public class DinerOrderAct extends AppCompatActivity {

    EditText et3,et2;
    TextView tv;
    Button btn;
    String st1;
    String dish;
    float price;
    String name = "yair";
    ListView lv;
    UserOrder uo;
    ArrayList<DishPrice> ArrDP;
    ArrayAdapter<String>adp;
    ArrayList<String>arrST;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diner_order);

        btn = (Button) findViewById(R.id.btn);
        et3 = (EditText) findViewById(R.id.et3);
        et2 = (EditText) findViewById(R.id.et2);
        lv = (ListView) findViewById(R.id.lv);
        tv = (TextView) findViewById(R.id.tv);

        ArrDP = new ArrayList<DishPrice>();
        arrST = new ArrayList<String>();
        adp = new ArrayAdapter<String>(DinerOrderAct.this , R.layout.support_simple_spinner_dropdown_item, arrST);
        lv.setAdapter(adp);
        ArrDP.clear();


    }



            public void AddDishPrice(View v) {


                dish = et2.getText().toString();
                price = Float.parseFloat(et3.getText().toString());

                DishPrice dp = new DishPrice(dish,price);

                String str = dish + " - " +  price;

                arrST.add(str);

                ArrDP.add(dp);

            }




    public void finish (View view) {


        UserOrder uo = new UserOrder(name , ArrDP);

        refEvnts.child(t).setValue(uo);


    }

}
