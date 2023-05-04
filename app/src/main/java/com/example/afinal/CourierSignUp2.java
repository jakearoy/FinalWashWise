
package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.model.VehicleType;

import java.util.HashMap;

import LoginSignup.AccountCreated;
import LoginSignup.Login;
import LoginSignup.SignUp;

public class CourierSignUp2 extends AppCompatActivity {
    String address;
    ImageView backBtn;
    Button signUp;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CheckBox mon, tues, wed, thur, fri, sat, sun, car, moped, bike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_sign_up2);

        signUp = findViewById(R.id.courier_sign_btn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCourier(v);
            }
        });

        backBtn = findViewById(R.id.signUp_back_arrow);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CourierSignUp.class);
                startActivity(intent);
                finish();
            }
        });


        car = findViewById(R.id.car_box);
        bike = findViewById(R.id.bike_box);
        moped = findViewById(R.id.moped_box);

    }

    public void createCourier(View view) {
        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("number");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        address = getIntent().getStringExtra("address");


        String vehicleType = "";
        if (car.isChecked()) {
            vehicleType = "car";
        } else if (moped.isChecked()) {
            vehicleType = "moped";
        } else if (bike.isChecked()) {
            vehicleType = "bike";
        }

        DatabaseReference couriersRef = FirebaseDatabase.getInstance().getReference("Couriers");
        String courierId = couriersRef.push().getKey();


        Courier courier = new Courier(courierId, name, phone, email, vehicleType, address);


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();


                            // Add user data to Realtime Database
                            DatabaseReference courierRef = FirebaseDatabase.getInstance().getReference("Couriers");
                            HashMap<String, Object> courierData = new HashMap<>();
                            courierData.put("name", courier.getName());
                            courierData.put("phone", courier.getPhone());
                            courierData.put("email", courier.getEmail());
                            courierData.put("address", courier.getAddress());
                            courierData.put("vehicleType", courier.getVehicleType());
                            courierData.put("ID", courier.getId());

                            courierRef.child(courierId).setValue(courierData);

                            // Redirect user to AccountCreated activity
                            Intent intent = new Intent(getApplicationContext(), AccountCreated.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CourierSignUp2.this, "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}


