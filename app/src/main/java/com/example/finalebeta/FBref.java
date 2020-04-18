package com.example.finalebeta;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBref {

        public static FirebaseAuth refAuth=FirebaseAuth.getInstance();

        public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
        public static DatabaseReference refUsers=FBDB.getReference("Users");
        public static DatabaseReference refEvnts=FBDB.getReference("Evnts");
        public static FirebaseStorage storage = FirebaseStorage.getInstance();
        public static StorageReference storageRef = storage.getReference();


}
