package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import static com.example.finalebeta.AddEventACT.EventID;
import static com.example.finalebeta.FBref.refAuth;
import static com.example.finalebeta.FBref.refEvnts;


public class FeedbackAct extends AppCompatActivity {

    RadioButton rb1,rb2,rb3,rb4,rb5;
    Button btnUpload,btnChoose;
    Button  btnSave,btnSend;
    EditText FBet;
    String melel;
    int rate;
    TextView tv;
    ArrayList<DishPrice> ARRDP;
    Evnts dataTMP1;
    String userUID;
    User Useruid;
    ArrayList<UserOrder>UO;
    String name;
    Double sum,MoneyP,change;
    int pos;
    String EVid;
    String FB,UID;
    String PlaceName;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri filePath;
    boolean bstorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnUpload=(Button)findViewById(R.id.btnUpload);
        btnChoose=(Button)findViewById(R.id.btnChoose);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnSend=(Button)findViewById(R.id.btnSend);
        FBet = (EditText) findViewById(R.id.FBet);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        rb4 = (RadioButton) findViewById(R.id.rb4);
        rb5 = (RadioButton) findViewById(R.id.rb5);
        tv=(TextView) findViewById(R.id.tv);
        UO = new ArrayList<UserOrder>();
        ARRDP = new ArrayList<DishPrice>();




        FirebaseUser firebaseUser = refAuth.getCurrentUser();
        UID=firebaseUser.getUid();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    upload();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choosepic();

            }
        });



        pos = getIntent().getExtras().getInt("key3");

        EVid = String.valueOf(EventID);

        Query query = refEvnts
                .orderByChild("id")
                .equalTo(EventID);
        query.addListenerForSingleValueEvent(vel);


    }

    /**
     * read all the order details for update the new details later
     *<p>
     */

    com.google.firebase.database.ValueEventListener vel = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot ds) {

            for(DataSnapshot data : ds.getChildren()) {
                dataTMP1 = data.getValue(Evnts.class);
                Useruid = data.getValue(User.class);

                FirebaseUser user = refAuth.getCurrentUser();
                userUID = user.getUid();
                UO = dataTMP1.getArrUO();

                bstorage = UO.get(pos).isStorage();
                PlaceName = dataTMP1.getPlace();
                name = UO.get(pos).getName();
                MoneyP = UO.get(pos).getMoneyPEID();
                change = UO.get(pos).getChange();
                sum = UO.get(pos).getTotalprice();
                ARRDP=UO.get(pos).getArrDP();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    /**
     *
     * the method set the review that write in the edittext in the textview
     * @param view
     * <p>
     */


        public void Send (View view) {

        melel = FBet.getText().toString();

        tv.setText(melel);

        FBet.setText("");
    }

    /**
     * this button open the gallery for choosing photo
     * <p>
     */

    private void choosepic () {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    /**
     *
     * this method uploading the photos that chose to firebase storage in folder of the event place name
     * @throws IOException
     * <p>
     */


    private void upload() throws IOException {

        if(filePath!=null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference reference =  storageReference.child(""+PlaceName+"/"+UID + ".jpg");
            reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    bstorage = true;
                    UserOrder uo = new UserOrder(name, ARRDP, sum, change, MoneyP, userUID, FB, rate,bstorage);

                    refEvnts.child(EVid).child("arrUO").child(String.valueOf(pos)).setValue(uo);
                    Toast.makeText(FeedbackAct.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    double progres = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progres+"%");

                }
            });


        }





        }

    /**
     *
     * this method write and update firebase with the new varibale , rate and review
     * @param view
     * <p>
     */

    public void save (View view) {

            if (rb1.isChecked()) rate = 1;
            if (rb2.isChecked()) rate = 2;
            if (rb3.isChecked()) rate = 3;
            if (rb4.isChecked()) rate = 4;
            if (rb5.isChecked()) rate = 5;

            if (rate == 0) {

                Toast.makeText(FeedbackAct.this, "Please rate us ", Toast.LENGTH_LONG);
            }
            else {

                FB = tv.getText().toString();



                UserOrder uo = new UserOrder(name, ARRDP, sum, change, MoneyP, userUID, FB, rate,bstorage);

                refEvnts.child(EVid).child("arrUO").child(String.valueOf(pos)).setValue(uo);

                Intent t = new Intent(this, OrderAct.class);
                startActivity(t);
            }

        }

    public boolean onCreateOptionsMenu (Menu menu) {

        menu.add("Open Events");
        menu.add("Credits");
        menu.add("Logout");

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item){

        String str = item.getTitle().toString();

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

        if (str.equals("Credits")) {

            Intent t = new Intent(this,Creditim.class);
            startActivity(t);
        }
        return true;
    }

}





