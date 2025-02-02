package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.finalebeta.AddEventACT.EventID;
import static com.example.finalebeta.FBref.refAuth;
import static com.example.finalebeta.FBref.refEvnts;

public class ShowFeedback extends AppCompatActivity {

    Button btn;
    ListView LV;
    EditText editText;
    TextView TV;
    ImageView imageView2;
    ImageButton plus,minus;
    int count = 0 , k=0;
    Evnts dataTMP;
    double Sumofrate = 0;
    double Rate = 0;
    String place;
    ArrayAdapter adp;
    ArrayList<String> ars = new ArrayList<>();
    ArrayList<String> arruid = new ArrayList<>();

    double AvgMark;



    StorageReference mStorageRef;
    public static StorageReference Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feedback);

        LV = (ListView)findViewById(R.id.LV);
        btn=(Button)findViewById(R.id.btn);
        TV=(TextView) findViewById(R.id.TV);
        editText = (EditText) findViewById(R.id.editText);
        imageView2 = findViewById(R.id.imageView2);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        /**
         * this is liseners for the button that can pass between the photos
         * <p>
         */

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(k==count - 1){
                    k=0;
                    downloadimg();
                }
                else{
                    k++;
                    downloadimg();
                }

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(k==0){
                    k=count-1;
                    downloadimg();
                }
                else{
                    k--;
                    downloadimg();
                }

            }
        });

    }

    /**
     * when you write the name of the place to check the review about him the method filter out the rate and the review only of the event that his place is the same
     * @param view
     * <p>
     */

    public void Search (View view) {

        place = editText.getText().toString();

        Query query = refEvnts
                .orderByChild("place")
                .equalTo(place);
        query.addListenerForSingleValueEvent(vel);

        editText.setHint("" + place);
        editText.setText("");
        TV.setHint(""+place);
        TV.setText("");

    }

    /**
     *
     * read from database only the rate and the review about this place
     * this method counter the raters and the total rate about this place
     * add to array list all the review about this place
     * calculate the average rate
     * <p>
     */

    com.google.firebase.database.ValueEventListener vel = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot ds) {

            if (ds.exists()) {

                count = 0;
                Rate = 0;
                Sumofrate=0;

                for (DataSnapshot data : ds.getChildren()) {

                    dataTMP = data.getValue(Evnts.class);


                    for (int j = 0;j < dataTMP.getArrUO().size() ; j++ ) {
                        if(dataTMP.getArrUO().get(j).getRate() != 0) {

                            Rate = Rate + (dataTMP.getArrUO().get(j).getRate());

                            Sumofrate++;
                        }
                        if (!TextUtils.isEmpty(dataTMP.getArrUO().get(j).getFeedback())){
                            ars.add(dataTMP.getArrUO().get(j).getFeedback());

                        }
                        if(dataTMP.getArrUO().get(j).isStorage()){
                            count++;
                            arruid.add(dataTMP.getArrUO().get(j).getUseruid());
                        }

                    }

                }
                AvgMark = Rate/Sumofrate;

                TV.setText("               " + place + " :" + "        "+ "Avrage Mark is :" + AvgMark + "     ");
                adp = new ArrayAdapter<>(ShowFeedback.this,R.layout.support_simple_spinner_dropdown_item, ars);
                LV.setAdapter(adp);
                downloadimg();


            }
            else{
                Toast.makeText(ShowFeedback.this, " There are no feedback's for this place  ", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /**
     * this method read from firebase storage from the folder of the place the photos and show them in imageview
     * <p>
     */

    private void downloadimg() {
        plus.setVisibility(View.VISIBLE);
        minus.setVisibility(View.VISIBLE);
        Ref = mStorageRef.child(""+place+"/").child("" + arruid.get(k) + ".jpg");
        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(ShowFeedback.this).load(uri).fit().centerCrop().into(imageView2);
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();

            }
        });


    }




    public boolean onCreateOptionsMenu (Menu menu) {

        menu.add("Open Events");
        menu.add("Credits");
        menu.add("Logout");

        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item){

        String str = item.getTitle().toString();


        if (str.equals("Credits")) {

            Intent t = new Intent(this,Creditim.class);
            startActivity(t);
        }

        if (str.equals("Open Events")) {

            Intent t = new Intent(this,AddEventACT.class);
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
