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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.finalebeta.FBref.refEvnts;

public class AddEventACT extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<String> IDlist = new ArrayList<>();
    ArrayList<Evnts> Values = new ArrayList<>();


    Button btn;
    ListView lv;
    ArrayAdapter adp;
    Evnts dataa;
    public static Long EventID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_a_c_t);

        btn=(Button)findViewById(R.id.btn);
        lv = (ListView)findViewById(R.id.lv);
        lv.setOnItemClickListener(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        /**

         * Method give today date
         *
         */

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime()); //פעוולה שמביאה את התאריך של היום
        currentDate = currentDate.replace('.','/');

        /**
         * Method filter out only the event with today date
         */

        Query query =  refEvnts.orderByChild("date").equalTo(currentDate);      // סינון אירועים שמופיעים לפי התאריך של אותו יום
        query.addListenerForSingleValueEvent(listener);

    }

    com.google.firebase.database.ValueEventListener listener = new ValueEventListener() {


        @Override
        public void onDataChange(@NonNull DataSnapshot ds) {


            IDlist.clear();
            Values.clear();

            for(DataSnapshot data : ds.getChildren()) {              //פעולה שקוראת מהפיירבייס את האירועים ודוחפת את הנתונים שלהם לתוך רשימה מטיפוס אירוע ומציגה את שם האירוע על ליסטויו

                Evnts dataTMP1 = data.getValue(Evnts.class);
                Values.add(dataTMP1);
                IDlist.add(dataTMP1.getName());
            }


            adp = new ArrayAdapter<>(AddEventACT.this,R.layout.support_simple_spinner_dropdown_item, IDlist);
            lv.setAdapter(adp);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {


        }
    };


    private void adapt() {

        adp = new ArrayAdapter<>(AddEventACT.this,R.layout.support_simple_spinner_dropdown_item, IDlist);
        lv.setAdapter(adp);
    }



    public void AddEvent (View view) {

        Intent t = new Intent(this,CreateEvent.class);
        startActivity(t);

    }

    /**
     * when you click on open event appears AlertDialog to insert event password
     * @param parent
     * @param view
     * @param position
     * @param id
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {  //פעולה שכאשר לוחצים על איבר/אירוע ברשימה נפתח אלרטדיאלוג שדורש להזין סיסמה יחודית לאותו אירוע

        dataa = Values.get(position);
        EventID  = dataa.getID();
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Put the password of " + dataa.getName());
        final EditText et = new EditText(this);
        et.setHint("Enter password :");
        adb.setView(et);
        adb.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = et.getText().toString();
                if (str.equals(dataa.getEpass()) ) {

                    Toast.makeText(AddEventACT.this, "correct ", Toast.LENGTH_SHORT).show();

                    Intent t = new Intent(AddEventACT.this,OrderAct.class);
                    startActivity(t);

                } else {

                    Toast.makeText(AddEventACT.this, "Incorrect password ", Toast.LENGTH_SHORT).show();

                }
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

    public boolean onCreateOptionsMenu (Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);

        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item){

        String str = item.getTitle().toString();


        if (str.equals("Credits")) {

            Intent t = new Intent(this,Creditim.class);
            startActivity(t);
        }

        if (str.equals("Review and recommendaions")) {

            Intent t = new Intent(this,ShowFeedback.class);
            startActivity(t);
        }

        return true;
    }




}
