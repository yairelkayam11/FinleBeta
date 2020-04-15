package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
    int i = 0;
    UserOrder dataa;
    String dataaName;
    Long pos,pos2;
    Evnts dataTMP;
    User Useruid;
    String userUID,SavedUID;
  //  UserOrder yair;






    ArrayList<String> Ast = new ArrayList<String>();
    ArrayList<UserOrder> UO = new ArrayList<UserOrder>();
    ArrayList<UserOrder> yair = new ArrayList<UserOrder>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        btn=(Button)findViewById(R.id.btn);
        lv = (ListView)findViewById(R.id.lv2);
        lv.setOnItemClickListener (this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);





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


                    FirebaseUser user = refAuth.getCurrentUser();
                    userUID = user.getUid();


                    if (dataTMP.getArrUO().isEmpty()) {

                        Toast.makeText(OrderAct.this, "There are no existing orders yet", Toast.LENGTH_SHORT).show();
                    }
                    for (int j = 0;j < dataTMP.getArrUO().size() ; j++ ) {

                        Ast.add( "The order of : "+dataTMP.getArrUO().get(j).getName());
                        UO.add(dataTMP.getArrUO().get(j));

                    }

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


        Intent t = new Intent(this,DinerOrderAct.class);
        t.putExtra("key",pos);
        startActivity(t);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        SavedUID = dataTMP.getArrUO().get(position).getUseruid();


        if (SavedUID.equals(userUID)) {

            dataa = UO.get(position);
            dataaName = dataa.getName();


            Intent t = new Intent(this,OrderDataPreview.class);
            t.putExtra("key",dataaName);
            startActivity(t);

        }
        else {

            Toast.makeText(OrderAct.this, "This user cannot access the edit window of this order", Toast.LENGTH_LONG).show();

        }

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

        return true;
     }

}
