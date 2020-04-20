package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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



   // Uri filePath;
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

        mStorageRef = FirebaseStorage.getInstance().getReference();  // משתנה שמכיל את כל ההרשאות דל סטורג

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

    public void Search (View view) {

        place = editText.getText().toString();
                                                        //כאשר לוחצים על כפתור "SEARCH" מתבצע תהליך סינון לפי מקום ת זאת אומרת רק האירועים שהם בעליי אותו place שהזנתי יקראו מהפיירבייס
        Query query = refEvnts
                .orderByChild("place")
                .equalTo(place);
        query.addListenerForSingleValueEvent(vel);

        editText.setHint("" + place);
        editText.setText("");
        TV.setHint(""+place);
        TV.setText("");

    }

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

                            Rate = Rate + (dataTMP.getArrUO().get(j).getRate());                    //פעולה שסוכמת את כל ציוני הדירוג שנתנו המבקרים וסוכמת גם את מספר המדרגים לצורך של חישוב ממוצע

                            Sumofrate++;
                        }
                        if (!TextUtils.isEmpty(dataTMP.getArrUO().get(j).getFeedback())){
                            ars.add(dataTMP.getArrUO().get(j).getFeedback());                                    //פעולה שדוחפת לתוך רשימה מטיפוס string את כל הביקורות שכתבו הסועדים ומציגה אותם אחר כך בליסטוויו

                        }
                        if(dataTMP.getArrUO().get(j).isStorage()){
                            count++;                                               //סכימה של כמות התמונות מstorage והוספתן לרשימת סטרינגים שבה יש את uid של מעלה התמונה
                            arruid.add(dataTMP.getArrUO().get(j).getUseruid());
                        }

                    }

                }
                AvgMark = Rate/Sumofrate;

                TV.setText("               " + place + " :" + "        "+ "Avrage Mark is :" + AvgMark + "     ");             //חישוב הציון הממוצע

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

    private void downloadimg() {
        plus.setVisibility(View.VISIBLE);
        minus.setVisibility(View.VISIBLE);
        Ref = mStorageRef.child(""+place+"/").child("" + arruid.get(k) + ".jpg");                             //פעולה ששולפת מהפיירבייס סטורג את התמונה האחרונה התקייה של אותו מקום אירוע ומציגה אותה בimage view
        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(ShowFeedback.this).load(uri).fit().centerCrop().into(imageView2);  //הצגת התמונה
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();

            }
        });


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

        if (str.equals("Open Events")) {

            Intent t = new Intent(this,AddEventACT.class);
            startActivity(t);
        }

        return true;
    }



}
