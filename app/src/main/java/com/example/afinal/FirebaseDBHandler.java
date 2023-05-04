package com.example.afinal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FirebaseDBHandler {
    private DatabaseReference mDatabase;

    public FirebaseDBHandler() {
        // Get a reference to the Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // Write data to the database
    public void writeData(String node, Object data) {
        mDatabase.child(node).setValue(data);
    }

    // Read data from the database
    public DatabaseReference readData(String node) {
        return mDatabase.child(node);
    }

    public void getCouriers(ValueEventListener listener){
        FirebaseDBHandler dbHandler = new FirebaseDBHandler();
        DatabaseReference couriersRef = dbHandler.readData("Couriers");
        couriersRef.addValueEventListener(listener);
    }

}

