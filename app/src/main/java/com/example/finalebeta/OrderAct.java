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
    Long pos,pos2;
    Evnts dataTMP;
    User Useruid;
    String userUID,SavedUID;
    double tip;
    double allprice;
  //  UserOrder yair;






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






        Query query = refEvnts
                .orderByChild("id")
                .equalTo(EventID);
        query.addListenerForSingleValueEvent(vel);   //פעולה שמסננת וציגה רק את ההזמנות של אותו אירוע שנכנסו אליו ושID שלו הוא אותו ID שח האירוע שבחרו


    }

   com.google.firebase.database.ValueEventListener vel = new ValueEventListener() {


        @Override
        public void onDataChange(@NonNull DataSnapshot ds) {
            if (ds.exists()) {
                for (DataSnapshot data : ds.getChildren()) {
                    dataTMP = data.getValue(Evnts.class);


                    FirebaseUser user = refAuth.getCurrentUser();  //קיראה מהדאטאבייס ומכניסה למשתמש את הUID של המשתמש שיצר הזמנה
                    userUID = user.getUid();


                    if (dataTMP.getArrUO().isEmpty()) {

                        Toast.makeText(OrderAct.this, "There are no existing orders yet", Toast.LENGTH_SHORT).show();//אם עדיין לאנפצחו הזמנות באותו אירוע זה כותה שאין הזמנות עדיין
                    }
                    for (int j = 0;j < dataTMP.getArrUO().size() ; j++ ) {
                        for (int k =0; k < dataTMP.getArrUO().get(j).getArrDP().size();k++) {

                            allprice = allprice + (dataTMP.getArrUO().get(j).getArrDP().get(k).getPrice());  // מכניסה לתוך רשימה מטיפוס UserOrder את כל המידע של הזמנות שנקראו ומציגה את שם המסועד ברשימה + סכימת כל התשלום של ההזמנה הכוללת

                        }
                        Ast.add("The order of : " + dataTMP.getArrUO().get(j).getName());
                        UO.add(dataTMP.getArrUO().get(j));

                    }

                    tip = allprice*0.12;  // פעולה לחישוב טיפ שנהוג לתת
                    TVtip.setText("Tip :"+ tip);


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


        Intent t = new Intent(this,DinerOrderAct.class);  //פעולת כפתור שמעבירה למסך שבו יוצרים הזמנה
        t.putExtra("key",pos);
        startActivity(t);


    }

    public boolean onCreateOptionsMenu (Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);

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

        return true;
     }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {


            SavedUID = dataTMP.getArrUO().get(position).getUseruid();


            if (SavedUID.equals(userUID)) {

                dataa = UO.get(position);                                          // כאשר לוחצים על הזמנה שנפתחה יש אפשרות רק לאותו user שפתח אותה לעבור למסך עריכת הזמנה
                dataaName = dataa.getName();


                Intent t = new Intent(this,OrderDataPreview.class);  // שלחית נתונים של שם סועד ומיקומו ברשימה
                t.putExtra("key",dataaName);
                t.putExtra("key2",position);
                startActivity(t);

            }
            else {

                Toast.makeText(OrderAct.this, "This user cannot access the edit window of this order", Toast.LENGTH_LONG).show(); // כאשר אדם מנשה להגיע למסך עריכה של הזמנה שלא הקים יקבל הודעה זו

            }

        }

    }

