package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.finalebeta.AddEventACT.EventID;

import static com.example.finalebeta.CreateEvent.t;
import static com.example.finalebeta.CreateEvent.t;
import static com.example.finalebeta.FBref.refAuth;
import static com.example.finalebeta.FBref.refEvnts;

import java.net.IDN;
import java.util.ArrayList;



public class OrderAct extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button btn;
    ArrayAdapter adp;
    ListView lv;
    TextView TVtip;
    int i = 0;
    UserOrder dataa;
    String dataaName;
    Long pos;
    Evnts dataTMP;
    String userUID,SavedUID;
    double tip;
    double allprice;







    ArrayList<String> Ast = new ArrayList<String>();
    ArrayList<UserOrder> UO = new ArrayList<UserOrder>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        btn=(Button)findViewById(R.id.btn);
        TVtip=(TextView) findViewById(R.id.TVtip);
        lv = (ListView)findViewById(R.id.lv2);
        lv.setOnItemClickListener (this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


/**
 *
 * this method filter out only the event with the same ID of the event that chose
 * <p>
 */



        Query query = refEvnts
                .orderByChild("id")
                .equalTo(EventID);
        query.addListenerForSingleValueEvent(vel);


    }

   com.google.firebase.database.ValueEventListener vel = new ValueEventListener() {


        @Override
        public void onDataChange(@NonNull DataSnapshot ds) {
            if (ds.exists()) {
                for (DataSnapshot data : ds.getChildren()) {
                    dataTMP = data.getValue(Evnts.class);

                    /**
                     * the method read from database the UID of the current user
                     * <p>
                     */


                    FirebaseUser user = refAuth.getCurrentUser();
                    userUID = user.getUid();


                    if (dataTMP.getArrUO().isEmpty()) {

                        Toast.makeText(OrderAct.this, "There are no existing orders yet", Toast.LENGTH_SHORT).show();
                    }

                    /**
                     *
                     * this method sum the whole price of all the diners and make tip
                     * <p>
                     */
                    for (int j = 0;j < dataTMP.getArrUO().size() ; j++ ) {

                        for(int t = 0 ; t<dataTMP.getArrUO().get(j).getArrDP().size() ;t++) {

                            allprice = allprice + (dataTMP.getArrUO().get(j).getArrDP().get(t).getPrice());
                        }

                        Ast.add("The order of : " + dataTMP.getArrUO().get(j).getName());
                        UO.add(dataTMP.getArrUO().get(j));

                    }
                    tip = allprice * 0.12;
                    TVtip.setText("Tip : " + tip);


                }
            }

            adp = new ArrayAdapter<>(OrderAct.this,R.layout.support_simple_spinner_dropdown_item, Ast);
            lv.setAdapter(adp);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void AddDiner (View view) {

        /**
         *
         * Method to pass between activities
         * <p>
         */


        Intent t = new Intent(this,DinerOrderAct.class);
        t.putExtra("key",pos);
        startActivity(t);


    }

    public boolean onCreateOptionsMenu (Menu menu) {

        menu.add("Open Events");
        menu.add("Credits");
        menu.add("Review and recommendations");
        menu.add("Logout");

        return true;

    }



     public boolean onOptionsItemSelected(MenuItem item){

        String str = item.getTitle().toString();

        if (str.equals("Open Events")) {

            Intent t = new Intent(this,AddEventACT.class);
            startActivity(t);
        }
         if (str.equals("Credits")) {

             Intent t = new Intent(this,Creditim.class);
             startActivity(t);
         }

         if (str.equals("Review and recommendations")) {

             Intent t = new Intent(this,ShowFeedback.class);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

        /**
         *
         * when click on order in the listview you pass to editorder activity but this option its only for the user that opened this order
         * by compareing the current uid to the uid of the user that opened
         * <p>
         */


        SavedUID = dataTMP.getArrUO().get(position).getUseruid();


            if (SavedUID.equals(userUID)) {

                dataa = UO.get(position);
                dataaName = dataa.getName();

                /**
                 *
                 * passing data , pass the name of the diner and the position of the order to read the order details of this position
                 * <p>
                 */


                Intent t = new Intent(this,OrderDataPreview.class);
                t.putExtra("key",dataaName);
                t.putExtra("key2",position);
                startActivity(t);

            }
            else {

                Toast.makeText(OrderAct.this, "This user cannot access the edit window of this order", Toast.LENGTH_LONG).show();

            }

        }

    }

