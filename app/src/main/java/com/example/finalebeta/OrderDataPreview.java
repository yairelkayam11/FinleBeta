package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import static com.example.finalebeta.AddEventACT.EventID;


import java.util.ArrayList;

import static com.example.finalebeta.CreateEvent.t;
import static com.example.finalebeta.FBref.refAuth;
import static com.example.finalebeta.FBref.refEvnts;

public class OrderDataPreview extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7,btnCancelName,btncancelDish,btncancelMP;
    ListView lv3;
    EditText EditName, ETdish, ETprice, ETmoney;
    TextView tv2, Tvtotalprice, TVchange, TVmoneypeid;
    String name;
    double MoneyP,change,sum;
    AlertDialog.Builder adb;
    int pos2;
    ArrayAdapter adp;
    ArrayList<DishPrice> ARRDP;
    ArrayList<String> Ast;
    ArrayList<UserOrder> ArrUO;
    ArrayList<UserOrder>UO;
    String dish;
    float price,priceTMP;
    int i = 0;
    String NameID,k;
    String Sprice;
    DishPrice dp;
    String Ssum,Smp,Schange;
    int j;
    Evnts dataTMP1;
    String EVid;
    int flag = 1;
    User Useruid;
    String userUID;
    int pos;

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
        btncancelDish = (Button) findViewById(R.id.btncancelDish);
        btnCancelName = (Button) findViewById(R.id.btnCancelName);
        btncancelMP = (Button) findViewById(R.id.btncancelMP);
        EditName = (EditText) findViewById(R.id.EditName);
        ETdish = (EditText) findViewById(R.id.ETdish);
        ETprice = (EditText) findViewById(R.id.ETprice);
        ETmoney = (EditText) findViewById(R.id.ETmoney);
        tv2 = (TextView) findViewById(R.id.tv2);
        Tvtotalprice = (TextView) findViewById(R.id.Tvtotalprice);
        TVchange = (TextView) findViewById(R.id.TVchange);
        TVmoneypeid = (TextView) findViewById(R.id.TVmoneypeid);
        lv3 = (ListView) findViewById(R.id.lv3);
        //lv3.setOnItemClickListener (this);
        lv3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ARRDP = new ArrayList<DishPrice>();
        Ast = new ArrayList<String>();
        ArrUO = new ArrayList<UserOrder>();
        UO = new ArrayList<UserOrder>();


        adp = new ArrayAdapter<>(OrderDataPreview.this,R.layout.support_simple_spinner_dropdown_item, Ast);
        lv3.setAdapter(adp);

        lv3.setOnItemClickListener(this);
        lv3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



        NameID = getIntent().getExtras().getString("key");
        pos = getIntent().getExtras().getInt("key2");

        Query query = refEvnts.orderByChild("id").equalTo(EventID);

        query.addListenerForSingleValueEvent(vel);      //מיון לפי אירוע שנכנסו אליו


         EVid = String.valueOf(EventID);


        tv2.setText("Order summary of" + " " + NameID);
    }



    com.google.firebase.database.ValueEventListener vel = new ValueEventListener() {


        @Override
        public void onDataChange(@NonNull DataSnapshot ds) {

            Ast.clear();
            ARRDP.clear();

            for(DataSnapshot data : ds.getChildren()) {
                  dataTMP1 = data.getValue(Evnts.class);
                  Useruid = data.getValue(User.class);

                FirebaseUser user = refAuth.getCurrentUser();
                userUID = user.getUid();


                UO = dataTMP1.getArrUO();

                for (int i = 0;i<UO.size();i++) {
                    if (NameID.equals(UO.get(i).getName())) {
                        j=i;
                    }
                }
                name = UO.get(j).getName();             //משיכת כל הנתונים של הזמנה של סועד j
                 MoneyP = UO.get(j).getMoneyPEID();
                change = UO.get(j).getChange();
                sum = UO.get(j).getTotalprice();
                ARRDP=UO.get(j).getArrDP();

                for(int i = 0; i<ARRDP.size(); i++){
                    Ast.add( ARRDP.get(i).getDish() + " - " + ARRDP.get(i).getPrice());
                }

                Tvtotalprice.setText("Total price :" +sum);
                TVchange.setText("Change :"+change);
                TVmoneypeid.setText("Money peid :"+MoneyP);


            }
            adp = new ArrayAdapter<>(OrderDataPreview.this,R.layout.support_simple_spinner_dropdown_item, Ast);
            lv3.setAdapter(adp);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }


    };


    public void EditName(View view) {

        EditName.setVisibility(View.VISIBLE);
        btn6.setVisibility(View.VISIBLE);
        btnCancelName.setVisibility(View.VISIBLE);  // פתיחת אופציה לעדכון שם

        EditName.setHint("Order name :");

    }
    public void ApplyName (View view) {

        name = EditName.getText().toString();     //

        if (name.isEmpty()) {
            Toast.makeText(OrderDataPreview.this, "You must enter diner name", Toast.LENGTH_SHORT).show();      //יישום השם החדש
        }
        else {
            tv2.setText("Order summary of" + " " + name);
            EditName.setVisibility(View.INVISIBLE);
            btn6.setVisibility(View.INVISIBLE);
            btnCancelName.setVisibility(View.INVISIBLE);
        }

    }
    public void cancelName (View view) {

        EditName.setVisibility(View.INVISIBLE);          //ביטול אופציית עריכת שם
        btn6.setVisibility(View.INVISIBLE);
        btnCancelName.setVisibility(View.INVISIBLE);

    }

    public void EditOrder (View view) {

        ETdish.setVisibility(View.VISIBLE);
        ETprice.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        btncancelDish.setVisibility(View.VISIBLE);         //פתחית אופצייה ללהוספת מנה
        ETdish.setHint("food name :");
        ETprice.setHint("price :");
    }

    public void add (View view) {

        dish = ETdish.getText().toString();
        price = Float.parseFloat(ETprice.getText().toString());

        if (dish.isEmpty()||ETprice.getText().toString().isEmpty()) {         //הוספת מנה

            Toast.makeText(OrderDataPreview.this, "you must enter price or dish name ", Toast.LENGTH_SHORT).show();


        }

             sum = sum + price;
             Ssum = String.valueOf(sum);

             dp = new DishPrice(dish, price);
             ARRDP.add(dp);
             ETdish.setVisibility(View.INVISIBLE);
             ETprice.setVisibility(View.INVISIBLE);
             btn3.setVisibility(View.INVISIBLE);
             btncancelDish.setVisibility(View.INVISIBLE);
             ETdish.setText("");
             ETprice.setText("");
             Ast.add(dish + " - " + price);  // עדכון המחיר לאחר הוספת מנה
             adp.notifyDataSetChanged();

             Tvtotalprice.setText("Total price :" + Ssum);


    }
    public void cancelDish (View view) {

        ETdish.setVisibility(View.INVISIBLE);
        ETprice.setVisibility(View.INVISIBLE);    //ביטול אופציית הוספת מנה
        btn3.setVisibility(View.INVISIBLE);
        btncancelDish.setVisibility(View.INVISIBLE);
    }

    public void MPedit (View view) {

        ETmoney.setVisibility(View.VISIBLE);
        btn7.setVisibility(View.VISIBLE);
        btncancelMP.setVisibility(View.VISIBLE);        //אפשרות לעריכת סכום לתשלום
        ETmoney.setHint("Money peid :");
    }
    public void ApplyMoney (View view) {

        MoneyP = Double.parseDouble(ETmoney.getText().toString());
        k = ETmoney.getText().toString();
        if (k.isEmpty()) Toast.makeText(OrderDataPreview.this, "You must enter money peid", Toast.LENGTH_SHORT).show();
        Smp = String.valueOf(MoneyP);
        TVmoneypeid.setText("Money peid :"+Smp);            //שינוי סכום לתשלום
        ETmoney.setVisibility(View.INVISIBLE);
        btn7.setVisibility(View.INVISIBLE);
        btncancelMP.setVisibility(View.INVISIBLE);

        change = MoneyP-sum;
        Schange = String.valueOf(change);
        TVchange.setText("Change :"+Schange);

    }

    public void cancelMP (View view) {

        ETmoney.setVisibility(View.INVISIBLE);
        btn7.setVisibility(View.INVISIBLE);             //ביטול אופציית עריכה סכום לתשלום
        btncancelMP.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        sum = sum - (dataTMP1.getArrUO().get(pos).getArrDP().get(position).getPrice());

        Tvtotalprice.setText(""+sum);

        adb=new AlertDialog.Builder(this);
        adb.setMessage("Do you want to delete this dish?");
        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {                //בלחיצה על איבר ברשימת המנות נפתחת אםשרות להסיר מנה ואחרי זה עדכון התשום
            @Override
            public void onClick(DialogInterface dialog, int which) {


                ARRDP.remove(position);
                dataTMP1.getArrUO().get(pos).setArrDP(ARRDP);
                refEvnts.child(""+EventID).setValue(dataTMP1);

                Ast.remove(position);
                adp.notifyDataSetChanged();

            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog ad = adb.create();
        ad.show();

    }

    public void back (View view) {

        UserOrder uo = new UserOrder(name,ARRDP,sum,change,MoneyP,userUID,null,0);

        refEvnts.child(EVid).child("arrUO").child(String.valueOf(j)).setValue(uo);                           //שמירת השינויים ועידכונם בפיירבייס



        Intent t = new Intent(this,OrderAct.class);
        startActivity(t);
    }

    public void DeletOrder (View view) {

        refEvnts.child(EVid).child("arrUO").child(String.valueOf(j)).removeValue();                  //כפתור למחיקת הזמנה

        Intent t = new Intent(this,OrderAct.class);
        startActivity(t);

    }

    public boolean onCreateOptionsMenu (Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item){

        String str = item.getTitle().toString();

        if (str.equals("Feedback")) {

            Intent t = new Intent(this,FeedbackAct.class);
            t.putExtra("key3",pos);
            startActivity(t);
        }
        if (str.equals("Open Events")) {

            Intent t = new Intent(this,AddEventACT.class);
            startActivity(t);
        }

        if (str.equals("Credits")) {

            Intent t = new Intent(this,Creditim.class);
            startActivity(t);
        }

        return true;
    }

}
