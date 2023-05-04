package LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.afinal.AdminHome;
import com.example.afinal.CourierHome;
import com.example.afinal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CourierLogin extends AppCompatActivity {

    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_login);

        backBtn = findViewById(R.id.back_arrow);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void CallHomeScreen(View view){

        TextInputLayout emailET = findViewById(R.id.email_login);
        TextInputLayout passwordET = findViewById(R.id.password_login);

        String email = emailET.getEditText().getText().toString();
        String password = passwordET.getEditText().getText().toString();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //user successfully authenitcated
                        Intent intent = new Intent(getApplicationContext(), CourierHome.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //user authentication failed
                        Toast.makeText(CourierLogin.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}