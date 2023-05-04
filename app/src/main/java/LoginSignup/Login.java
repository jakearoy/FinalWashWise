package LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.afinal.HomeScreen;
import com.example.afinal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    ImageView backBtn;
    Button forgotBtn;
    public FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        backBtn = findViewById(R.id.back_arrow);
        forgotBtn = findViewById(R.id.forgot_Pw_btn);
        mAuth = FirebaseAuth.getInstance(); // Initialize mAuth


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Welcome.class);
                startActivity(intent);
                finish();
            }
        });
        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeSelection.class);
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
                            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //user authentication failed
                            Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    public void openAdminLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), AdminLogin.class);
        startActivity(intent);
        finish();
    }

    public void openCourierLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), CourierLogin.class);
        startActivity(intent);
        finish();

    }
}