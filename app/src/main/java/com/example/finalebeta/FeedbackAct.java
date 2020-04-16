package com.example.finalebeta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import static com.example.finalebeta.AddEventACT.EventID;
import static com.example.finalebeta.FBref.FBST;
import static com.example.finalebeta.FBref.refAuth;
import static com.example.finalebeta.FBref.refEvnts;
import static com.example.finalebeta.FBref.refImages;
import static com.example.finalebeta.FBref.refStor;

public class FeedbackAct extends AppCompatActivity {

    RadioButton rb1,rb2,rb3,rb4,rb5;
    Button btnUpload,btnChoose;
    Button  btnSave,btnSend;
    EditText FBet;
    String rate,melel;
    TextView tv;
    int Gallery=1;
    ArrayList<DishPrice> ARRDP;
    Evnts dataTMP1;
    int j;
    String userUID,NameID;
    User Useruid;
    ArrayList<UserOrder>UO;
    String name;
    Double sum,MoneyP,change;
    int pos;
    String EVid;
    String FB;
    ImageView iV;
    Uri filePath;

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


        if (rb1.isChecked()) rate = "1";
        if (rb2.isChecked()) rate = "2";
        if (rb3.isChecked()) rate = "3";
        if (rb4.isChecked()) rate = "4";
        if (rb5.isChecked()) rate = "5";

        ValueEventListener listener = new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                for(DataSnapshot data : ds.getChildren()) {
                    dataTMP1 = data.getValue(Evnts.class);
                    Useruid = data.getValue(User.class);

                    FirebaseUser user = refAuth.getCurrentUser();
                    userUID = user.getUid();
                    UO = dataTMP1.getArrUO();

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
        refEvnts.addValueEventListener(listener);
    }




    public void Send (View view) {

        melel = FBet.getText().toString();

        tv.setText(melel);

        FBet.setText("");
    }

    private void choosepic () {

        Intent si = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(si, Gallery);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Gallery) {
                Uri file = data.getData();
                if (file != null) {
                    final ProgressDialog pd=ProgressDialog.show(this,"Upload image","Uploading...",true);
                    StorageReference refImg = refImages.child("aaa.jpg");
                    refImg.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    pd.dismiss();
                                    Toast.makeText(FeedbackAct.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    pd.dismiss();
                                    Toast.makeText(FeedbackAct.this, "Upload failed", Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "No Image was selected", Toast.LENGTH_LONG).show();
                }
            }
        }
    }



    private void upload() throws IOException {

        final ProgressDialog pd=ProgressDialog.show(this,"Image download","downloading...",true);

        StorageReference refImg = refImages.child("aaa.jpg");

        final File localFile = File.createTempFile("aaa","jpg");
        refImg.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(FeedbackAct.this, "Image download success", Toast.LENGTH_LONG).show();
                String filePath = localFile.getPath();
                //Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                //iV.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                Toast.makeText(FeedbackAct.this, "Image download failed", Toast.LENGTH_LONG).show();
            }
        });



        }

        public void save (View view) {

             FB = tv.getText().toString();


            UserOrder uo = new UserOrder(name,ARRDP,sum,change,MoneyP,userUID,FB,rate);

            refEvnts.child(EVid).child("arrUO").child(String.valueOf(j)).setValue(uo);

            Intent t = new Intent(this,OrderAct.class);
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

}





