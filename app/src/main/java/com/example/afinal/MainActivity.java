package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;

import LoginSignup.Welcome;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private static int SPASH_SCREEN = 4500;

    //variables
    Animation topAnim, bottomAnim;
    ImageView imageView;
    GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);


        //animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

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
        },SPASH_SCREEN);
    }
}