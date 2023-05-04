package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import LoginSignup.Welcome;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseRef;
    private static int SPASH_SCREEN = 4500;

    //variables
    Animation topAnim, bottomAnim;
    ImageView imageView;
    GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        Places.initialize(getApplicationContext(), "AIzaSyAVgEhE3YwAoQnqajcewuqqhdNyUoPuUMg");

        PlacesClient placesClient = Places.createClient(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference adminRef = database.getReference("admin");

        String uid = "OkHT6iIKI3gMvi8y0LXNPo9oXCw1";
        String email = "adminjake@gmail.com";
        String address = "address";
        String name = "Admin";
        String phone = "555-555-5555";

        User adminUser = new User(address, email, name, phone, true);
        adminRef.child(uid).setValue(adminUser);


        if (FirebaseDatabase.getInstance() == null) {
            Log.e("TAG", "Firebase Realtime Database is not initialized!");
        } else {
            Log.d("TAG", "Firebase Realtime Database is initialized.");
        }

        //animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //hooks
        imageView = findViewById(R.id.logo);
        gifImageView = findViewById(R.id.loading);

        imageView.setAnimation(topAnim);
        gifImageView.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Welcome.class);
                startActivity(intent);
                finish();
            }
        }, SPASH_SCREEN);
    }
}