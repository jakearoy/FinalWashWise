package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import LoginSignup.Login;

public class AdminHome extends AppCompatActivity {

    public String address;
    private String adminToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        adminToken = getTokenResult.getToken();
                        Log.d("AUTH_TOKEN", adminToken);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AUTH_TOKEN", "Error getting authentication token", e);
                    }
                });

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.laundry_autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
        autocompleteFragment.setCountry("UK"); // Optional filter by country
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS); // Filter for addresses only

        // Set the PlaceSelectionListener to handle the selected location
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                address = place.getAddress();

            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle any errors
            }
        });

    }

    public void addLaundromat(View view) {
        // Get a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference laundromatsRef = database.getReference("laundromats");

        // Get the input values from the EditText fields
        TextInputLayout nameTextInputLayout = findViewById(R.id.laundryName);
        EditText nameEditText = nameTextInputLayout.getEditText();
        String name = nameEditText.getText().toString();

        TextInputLayout numberTextInputLayout = findViewById(R.id.laundryNumber);
        EditText numberEditText = numberTextInputLayout.getEditText();
        String number = numberEditText.getText().toString();

        TextInputLayout ironTextInputLayout = findViewById(R.id.iron_price);
        EditText ironEditText = ironTextInputLayout.getEditText();
        double ironPrice = Double.parseDouble(ironEditText.getText().toString());

        TextInputLayout washTextInputLayout = findViewById(R.id.wash_price);
        EditText washEditText = washTextInputLayout.getEditText();
        double washPrice = Double.parseDouble(washEditText.getText().toString());


        TextInputLayout dryCleanTextInputLayout = findViewById(R.id.dryclean_price);
        EditText dryCleanEditText = dryCleanTextInputLayout.getEditText();
        double dryCleanPrice = Double.parseDouble(dryCleanEditText.getText().toString());

        TextInputLayout washIronTextInputLayout = findViewById(R.id.wash_iron_price);
        EditText washIronEditText = washIronTextInputLayout.getEditText();
        double washIronPrice = Double.parseDouble(washIronEditText.getText().toString());

        // Create a new laundromat object with the input values and selected location
        Laundromat newLaundromat = new Laundromat(name, address, dryCleanPrice, ironPrice, washPrice, number, washIronPrice);

        // Add the new laundromat to the database
        DatabaseReference newLaundromatRef = laundromatsRef.push();
        newLaundromatRef.setValue(newLaundromat);


        Toast.makeText(AdminHome.this, "New laundromat added to database", Toast.LENGTH_SHORT).show();


    }
}
