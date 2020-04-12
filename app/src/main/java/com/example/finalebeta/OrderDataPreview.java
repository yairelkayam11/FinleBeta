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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.finalebeta.CreateEvent.t;
import static com.example.finalebeta.FBref.refEvnts;

public class OrderDataPreview extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;
    ListView lv3;
    EditText EditName, ETdish, ETprice, ETmoney;
    TextView tv1, tv2, Tvtotalprice, TVchange, TVmoneypeid;
    String name;
    double MoneyP,change,sum;
    AlertDialog.Builder adb;
    int pos2;
    ArrayAdapter adp;
    ArrayList<DishPrice> ARRDP;
    ArrayList<String> Ast;
    ArrayList<UserOrder> ArrUO;
    String dish;
    float price,priceTMP;
    int i = 0;
    int pos = 0;
    String Sprice;
    DishPrice dp;
    String Ssum,Smp,Schange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_data_preview);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        EditName = (EditText) findViewById(R.id.EditName);
        ETdish = (EditText) findViewById(R.id.ETdish);
        ETprice = (EditText) findViewById(R.id.ETprice);
        ETmoney = (EditText) findViewById(R.id.ETmoney);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        Tvtotalprice = (TextView) findViewById(R.id.Tvtotalprice);
        TVchange = (TextView) findViewById(R.id.TVchange);
        TVmoneypeid = (TextView) findViewById(R.id.TVmoneypeid);
        lv3 = (ListView) findViewById(R.id.lv3);
        lv3.setOnItemClickListener (this);
        lv3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ARRDP = new ArrayList<DishPrice>();
        Ast = new ArrayList<String>();
        ArrUO = new ArrayList<UserOrder>();



        ValueEventListener vel = new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                Ast.clear();
                ARRDP.clear();

                for(DataSnapshot data : ds.getChildren()) {
                    Evnts dataTMP1 = data.getValue(Evnts.class);
                    Intent t = new Intent();
                    pos = t.getIntExtra("key",pos2);
                    name = dataTMP1.getArrUO().get(pos).getName();
                    MoneyP = dataTMP1.getArrUO().get(pos).getMoneyPEID();
                    change = dataTMP1.getArrUO().get(pos).getChange();
                    sum = dataTMP1.getArrUO().get(pos).getTotalprice();


                    while(i<dataTMP1.getArrUO().size()) {
                      dish = dataTMP1.getArrUO().get(pos).getArrDP().get(i).getDish();
                      price = dataTMP1.getArrUO().get(pos).getArrDP().get(i).getPrice();

                      dp = new DishPrice(dish,price);

                       ARRDP.add(dp);

                       Sprice = String.valueOf(price);

                        Ast.add(dish+ " " +Sprice);
                        i++;
                    }
                }
                adp = new ArrayAdapter<>(OrderDataPreview.this,R.layout.support_simple_spinner_dropdown_item, Ast);
                lv3.setAdapter(adp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        refEvnts.child("Evnts").addValueEventListener(vel);

        tv1.setText("Order summary of" + " " + name);
        tv2.setText(name);
    }

    public void EditName(View view) {

        EditName.setVisibility(View.VISIBLE);
        btn6.setVisibility(View.VISIBLE);
        EditName.setHint("Order name :");

    }
    public void ApplyName (View view) {

        name = EditName.getText().toString();
        tv1.setText("Order summary of" + " " + name);
        tv2.setText(name);
        EditName.setVisibility(View.INVISIBLE);
        btn6.setVisibility(View.INVISIBLE);
    }

    public void EditOrder (View view) {

        ETdish.setVisibility(View.VISIBLE);
        ETprice.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        ETdish.setHint("food name :");
        ETprice.setHint("price :");
    }

    public void add (View view) {

        dish = ETdish.getText().toString();
        price = Float.parseFloat(ETprice.getText().toString());

        sum = sum + price;
        Ssum = String.valueOf(sum);

        dp = new DishPrice(dish,price);
        ARRDP.add(dp);
        ETdish.setVisibility(View.INVISIBLE);
        ETprice.setVisibility(View.INVISIBLE);
        Ast.add(dish+ " " +Sprice);
        adp.notifyDataSetChanged();

        Tvtotalprice.setText(Ssum);
    }

    public void MPedit (View view) {

        ETmoney.setVisibility(View.VISIBLE);
        btn7.setVisibility(View.VISIBLE);
        ETmoney.setHint("Money peid :");
    }
    public void ApplyMoney (View view) {

        MoneyP = Double.parseDouble(ETmoney.getText().toString());
        Smp = String.valueOf(MoneyP);
        TVmoneypeid.setText(Smp);
        ETmoney.setVisibility(View.INVISIBLE);
        btn7.setVisibility(View.INVISIBLE);

        change = MoneyP-sum;
        Schange = String.valueOf(change);
        TVchange.setText(Schange);

    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        adb=new AlertDialog.Builder(this);
        adb.setMessage("Do you want to delete this dish?");
        adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                refEvnts.child("DinerOrder").child(String.valueOf(pos)).child("arrDP").child(String.valueOf(position)).removeValue();

                Ast.remove(position);
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

    public void back (View view) {

        UserOrder uo = new UserOrder(name,ARRDP,sum,change,MoneyP);
        ArrUO.add(uo);
        refEvnts.child(t).child("DinerOrder").child("" + name).setValue(ArrUO);


        Intent t = new Intent(this,OrderAct.class);
        startActivity(t);
    }

    public void DeletOrder (View view) {

        refEvnts.child("DinerOrder").child(String.valueOf(pos)).removeValue();

        Intent t = new Intent(this,OrderAct.class);
        startActivity(t);

    }
}
