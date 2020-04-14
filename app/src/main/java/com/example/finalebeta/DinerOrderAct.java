package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.EventLog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.finalebeta.CreateEvent.t;
import static com.example.finalebeta.AddEventACT.EventID;

import static com.example.finalebeta.CreateEvent.t;
import static com.example.finalebeta.FBref.refUsers;
import static com.example.finalebeta.FBref.refEvnts;
import static java.util.Objects.*;


public class DinerOrderAct extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText et3,et2,ETname,et14;
    TextView tv,tv18,tv17;
    Button btn;
    String st1;
    String dish;
    float price;
    String namee ;
    ListView lv;
    UserOrder uo;
    ArrayList<DishPrice> ArrDP;
    ArrayAdapter<String>adp;
    ArrayList<String>arrST;
    String place, Epass,time,date,name;
    Long IDD;
    boolean Active;
    Double sum=0.0;
    String Ssum;
    AlertDialog.Builder adb;
    int FriendsSum = 0;
    ArrayList<UserOrder> ArrUO;
    Double MoneyP;
    Double change;
    String STRMoneyP,Schange;
    Evnts dataTMP;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diner_order);

        btn = (Button) findViewById(R.id.btn);
        et3 = (EditText) findViewById(R.id.et3);
        et2 = (EditText) findViewById(R.id.et2);
        et14 = (EditText) findViewById(R.id.et14);
        lv = (ListView) findViewById(R.id.lv);
        ETname = (EditText) findViewById(R.id.ETname);
        tv17 = (TextView) findViewById(R.id.tv17);
        tv18 = (TextView) findViewById(R.id.tv18);

        ArrDP = new ArrayList<DishPrice>();
        ArrUO = new ArrayList<UserOrder>();
        arrST = new ArrayList<String>();
        adp = new ArrayAdapter<String>(DinerOrderAct.this , R.layout.support_simple_spinner_dropdown_item, arrST);
        lv.setAdapter(adp);




        ArrDP.clear();
        t= EventID;

        Query query = refEvnts
                .orderByChild("id")
                .equalTo(t);
        query.addListenerForSingleValueEvent(vel);



    }


    com.google.firebase.database.ValueEventListener vel = new ValueEventListener() {


        @Override
        public void onDataChange(@NonNull DataSnapshot ds) {
            if (ds.exists()) {
                for (DataSnapshot data : ds.getChildren()) {
                    dataTMP = data.getValue(Evnts.class);

                }
            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public void apply (View view) {

        namee = ETname.getText().toString();

        ETname.setText("");

    }



            public void AddDishPrice(View v) {


                dish = et2.getText().toString();
                price = Float.parseFloat(et3.getText().toString());

                sum = sum + price;

                Ssum= String.valueOf(sum);

                tv18.setText(Ssum);

                String g = et3.getText().toString();;

                if (dish.isEmpty()) Toast.makeText(DinerOrderAct.this, "you must enter a dish name ", Toast.LENGTH_SHORT).show();
                if (g.isEmpty()) Toast.makeText(DinerOrderAct.this, "you must enter a price ", Toast.LENGTH_SHORT).show();

                DishPrice dp = new DishPrice(dish,price);


                String str = dish + " - " +  price;

                arrST.add(str);

                ArrDP.add(dp);

                et2.setText("");
                et3.setText("");

            }












    public void finish (View view) {


        STRMoneyP = et14.getText().toString();

        MoneyP = Double.valueOf((STRMoneyP));


        change = MoneyP-sum;

        Schange = String.valueOf(change);
        tv17.setText(Schange);

        ArrUO=dataTMP.getArrUO();
        UserOrder uo = new UserOrder(namee,ArrDP,sum,change,MoneyP);

       ArrUO.add(uo);

        dataTMP.setArrUO(ArrUO);
        refEvnts.child(""+t).setValue(dataTMP);




        //refEvnts.child(""+t).child("DinerOrder").child("" + namee).setValue(ArrUO);

        Toast.makeText(DinerOrderAct.this, "Your order has been received", Toast.LENGTH_SHORT).show();

        FriendsSum++;

        Intent h = new Intent(this,OrderAct.class);
        startActivity(h);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        adb=new AlertDialog.Builder(this);
        adb.setMessage("Do you want to delete this dish?");
        adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                refEvnts.child("DinerOrder").child(String.valueOf(FriendsSum)).child("arrDP").child(String.valueOf(position)).removeValue();

                arrST.remove(position);
                adp.notifyDataSetChanged();

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
