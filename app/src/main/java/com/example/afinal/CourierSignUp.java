package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;

import LoginSignup.AccountCreated;
import LoginSignup.Login;
import LoginSignup.SignUp;

public class CourierSignUp extends AppCompatActivity {
    String address;
    ImageView backBtn;
    TextInputLayout fullName, PhoneNo, Email, Password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_sign_up);

        backBtn = findViewById(R.id.signUp_back_arrow);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });



        fullName = findViewById(R.id.signUpFullName);
        Email = findViewById(R.id.signUpEmail);
        Password = findViewById(R.id.signUpPassword);
        PhoneNo = findViewById(R.id.phoneNum);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
        autocompleteFragment.setCountry("UK"); // Optional filter by country
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS); // Filter for addresses only


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Handle the selected place
                address = place.getAddress();
            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle any errors
            }
        });

    }





    //validation functions

    private boolean validateFullName() {
        String val = fullName.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            fullName.setError("Field can not be empty");
            return false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }

    }


    private boolean validateEmail() {
        String val = Email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            Email.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            Email.setError("Invalid Email");
            return false;
        } else {
            Email.setError(null);
            Email.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {
        String val = Password.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                // (?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                // "(?=S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";


        if (val.isEmpty()) {
            Password.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            Password.setError("Password should contain: Atleast 4 characters, 1 uppercase letter and no whitespaces");
            return false;
        } else {
            Password.setError(null);
            Password.setErrorEnabled(false);
            return true;
        }
    }

    public void callNextSignUpScreen(View view) {
        if (!validateFullName() | !validateEmail() | !validatePassword()) {
            return;
        }

        String fullNameText = fullName.getEditText().getText().toString().trim();
        String emailText = Email.getEditText().getText().toString().trim();
        String passwordText = Password.getEditText().getText().toString().trim();
        String phoneNoText = PhoneNo.getEditText().getText().toString().trim();


        Intent intent = new Intent(getApplicationContext(), CourierSignUp2.class);
        intent.putExtra("name",fullNameText);
        intent.putExtra("address",address);
        intent.putExtra("email",emailText);
        intent.putExtra("password",passwordText);
        intent.putExtra("number",phoneNoText);
        startActivity(intent);
    }
}

