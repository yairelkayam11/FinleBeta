package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7,btn11,btnCancelName,btncancelDish,btncancelMP;
    ListView lv3;
    EditText EditName, ETdish, ETprice, ETmoney;
    TextView tv2, Tvtotalprice, TVchange, TVmoneypeid;
    String name;
    double MoneyP,change,sum;
    AlertDialog.Builder adb;
    int Rate;
    ArrayAdapter adp;
    ArrayList<DishPrice> ARRDP;
    ArrayList<String> Ast;
    ArrayList<UserOrder> ArrUO;
    ArrayList<UserOrder>UO;
    String dish;
    float price;
    String NameID;
    DishPrice dp;
    String Ssum,Smp,Schange;
    int j;
    Evnts dataTMP1;
    String EVid;
    User Useruid;
    String userUID,fb;
    int pos;
    boolean storage;

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
        btn11 = (Button) findViewById(R.id.btn11);
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
        lv3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ARRDP = new ArrayList<DishPrice>();
        Ast = new ArrayList<String>();
        ArrUO = new ArrayList<UserOrder>();
        UO = new ArrayList<UserOrder>();


        adp = new ArrayAdapter<>(OrderDataPreview.this,R.layout.support_simple_spinner_dropdown_item, Ast);
        lv3.setAdapter(adp);

        lv3.setOnItemClickListener(this);
        lv3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        /**
         * save the diner name and the position of the order that checked in last activity to open the details of this order in edit act
         * filter out the event that checked
         * <p>
         */



        NameID = getIntent().getExtras().getString("key");
        pos = getIntent().getExtras().getInt("key2");

        Query query = refEvnts.orderByChild("id").equalTo(EventID);

        query.addListenerForSingleValueEvent(vel);


         EVid = String.valueOf(EventID);


        tv2.setText("Order summary of" + " " + NameID);
    }



    com.google.firebase.database.ValueEventListener vel = new ValueEventListener() {

        /**
         * read from database the details of the order of the order of the name
         * @param ds
         * <p>
         */


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
                storage = UO.get(j).isStorage();
                name = UO.get(j).getName();
                 MoneyP = UO.get(j).getMoneyPEID();
                change = UO.get(j).getChange();
                sum = UO.get(j).getTotalprice();
                ARRDP = UO.get(j).getArrDP();
                Rate = UO.get(j).getRate();
                fb = UO.get(j).getFeedback();



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
        btnCancelName.setVisibility(View.VISIBLE);

        EditName.setHint("Order name :");

    }

    /**
     *
     * save the new name
     * @param view
     * <p>
     */

    public void ApplyName (View view) {

        name = EditName.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(OrderDataPreview.this, "You must enter diner name", Toast.LENGTH_SHORT).show();
        }
        else {
            tv2.setText("Order summary of" + " " + name);
            EditName.setVisibility(View.INVISIBLE);
            btn6.setVisibility(View.INVISIBLE);
            btnCancelName.setVisibility(View.INVISIBLE);
        }

    }

    public void cancelName (View view) {

        EditName.setVisibility(View.INVISIBLE);
        btn6.setVisibility(View.INVISIBLE);
        btnCancelName.setVisibility(View.INVISIBLE);

    }

    public void EditOrder (View view) {

        ETdish.setVisibility(View.VISIBLE);
        ETprice.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        btncancelDish.setVisibility(View.VISIBLE);
        ETdish.setHint("food name :");
        ETprice.setHint("price :");
    }

    /**
     * add the dish and price to the dish list
     * @param view
     * <p>
     */

    public void add (View view) {

        if (!TextUtils.isEmpty(ETdish.getText().toString())&&!TextUtils.isEmpty(ETprice.getText().toString())) {

            dish = ETdish.getText().toString();
            price = Float.parseFloat(ETprice.getText().toString());

            sum = sum + price;


          if (sum<=MoneyP&&price<=MoneyP) {

              Ssum = String.valueOf(sum);

              dp = new DishPrice(dish, price);
              ARRDP.add(dp);
              ETdish.setVisibility(View.INVISIBLE);
              ETprice.setVisibility(View.INVISIBLE);
              btn3.setVisibility(View.INVISIBLE);
              btncancelDish.setVisibility(View.INVISIBLE);
              ETdish.setText("");
              ETprice.setText("");
              Ast.add(dish + " - " + price);
              adp.notifyDataSetChanged();

              change = MoneyP - sum;

              Tvtotalprice.setText("Total price :" + Ssum);
              TVchange.setText("Change :" + (MoneyP - sum));
          }
          else {
              Toast.makeText(OrderDataPreview.this, "You must update your money peid ", Toast.LENGTH_SHORT).show();
              sum = sum - price;
              ETdish.setText("");
              ETprice.setText("");
          }
        }

        else  {

            Toast.makeText(OrderDataPreview.this, "You must enter price or dish name ", Toast.LENGTH_SHORT).show();

        }


    }
    public void cancelDish (View view) {

        ETdish.setVisibility(View.INVISIBLE);
        ETprice.setVisibility(View.INVISIBLE);
        btn3.setVisibility(View.INVISIBLE);
        btncancelDish.setVisibility(View.INVISIBLE);
    }

    public void MPedit (View view) {

        ETmoney.setVisibility(View.VISIBLE);
        btn7.setVisibility(View.VISIBLE);
        btncancelMP.setVisibility(View.VISIBLE);
        ETmoney.setHint("Money peid :");
    }

    public void ApplyMoney (View view) {

        if (!TextUtils.isEmpty(ETmoney.getText().toString())) {

            MoneyP = Double.parseDouble(ETmoney.getText().toString());
            Smp = String.valueOf(MoneyP);
            TVmoneypeid.setText("Money peid :" + Smp);
            ETmoney.setVisibility(View.INVISIBLE);
            btn7.setVisibility(View.INVISIBLE);
            btncancelMP.setVisibility(View.INVISIBLE);

            change = MoneyP - sum;
            Schange = String.valueOf(change);
            TVchange.setText("Change :" + Schange);
        }

        else {
            Toast.makeText(OrderDataPreview.this, "You must enter money peid", Toast.LENGTH_SHORT).show();
        }

    }

    public void cancelMP (View view) {

        ETmoney.setVisibility(View.INVISIBLE);
        btn7.setVisibility(View.INVISIBLE);
        btncancelMP.setVisibility(View.INVISIBLE);

    }

    /**
     *
     * by click on item from the dish list you can remove him
     * @param parent
     * @param view
     * @param position
     * @param id
     * <p>
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {



        adb=new AlertDialog.Builder(this);
        adb.setMessage("Do you want to delete this dish?");
        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                sum = sum - (dataTMP1.getArrUO().get(pos).getArrDP().get(position).getPrice());

                Tvtotalprice.setText("Total price :"+sum);
                TVchange.setText("Change :"+(MoneyP-sum));


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

    /**
     *
     * save all the changes of the order and pushing them to firebase
     * @param view
     * <p>
     */

    public void back (View view) {

        UserOrder uo = new UserOrder(name,ARRDP,sum,change,MoneyP,userUID,fb,Rate,storage);

        refEvnts.child(EVid).child("arrUO").child(String.valueOf(j)).setValue(uo);



        Intent t = new Intent(this,OrderAct.class);
        startActivity(t);
    }

    /**
     *
     * when you push this button apper dialog the can delete the whole order from database
     * @param view
     */

    public void DeletOrder (View view) {



        adb=new AlertDialog.Builder(this);
        adb.setMessage("Do you want to delete this order?");
        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                //כפתור למחיקת הזמנה

                UO = dataTMP1.getArrUO();
                int size = dataTMP1.getArrUO().size();

                for (int p = j+1 ; p < size ; p++) {

                    storage = UO.get(p).isStorage();
                    name = UO.get(p).getName();
                    MoneyP = UO.get(p).getMoneyPEID();
                    change = UO.get(p).getChange();
                    sum = UO.get(p).getTotalprice();
                    ARRDP = UO.get(p).getArrDP();
                    Rate = UO.get(p).getRate();
                    fb = UO.get(p).getFeedback();

                    UserOrder userOrder = new UserOrder(name,ARRDP,sum,change,MoneyP,userUID,fb,Rate,storage);

                    refEvnts.child(EVid).child("arrUO").child(String.valueOf(j)).setValue(userOrder);

                    refEvnts.child(EVid).child("arrUO").child(String.valueOf(p)).removeValue();

                    j++;


                }

                if (j==size-1) {
                    refEvnts.child(EVid).child("arrUO").child(String.valueOf(j)).removeValue();
                }


                Intent t = new Intent(OrderDataPreview.this,OrderAct.class);
                startActivity(t);


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

    public boolean onCreateOptionsMenu (Menu menu) {


        menu.add("Feedback");
        menu.add("Open Events");
        menu.add("Credits");
        menu.add("Logout");

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
