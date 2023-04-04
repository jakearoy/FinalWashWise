package LoginSignup;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.afinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class SignUp3 extends AppCompatActivity {
    ImageView backBtn;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        backBtn = findViewById(R.id.signUp_back_arrow3);
        phoneNumber = findViewById(R.id.phoneNum);
        countryCodePicker = findViewById(R.id.countryPicker);
        mAuth = FirebaseAuth.getInstance();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp2.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean validatePhoneNumber() {
        String val = phoneNumber.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            phoneNumber.setError("Enter valid phone number");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }
    }

    public void callAccountCreated(View view) {
        if (!validatePhoneNumber()) {
            return;
        }

        Intent intentFromPrevActivity = getIntent();
        String fullName = intentFromPrevActivity.getStringExtra("FullName");
        String username = intentFromPrevActivity.getStringExtra("Username");
        String email = intentFromPrevActivity.getStringExtra("Email");
        String password = intentFromPrevActivity.getStringExtra("Password");
        String phone = phoneNumber.getEditText().getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            // Save user data to the Firebase Realtime Database
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("fullName", fullName);
                            userData.put("username", username);
                            userData.put("email", email);
                            userData.put("phoneNumber", phone); // Use the phone variable instead of phoneNumber
                            reference.setValue(userData);

                            Toast.makeText(SignUp3.this, "Account Created.", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUp3.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error creating user: " + e.getMessage());
                    }
                });

        Intent intent = new Intent(getApplicationContext(), AccountCreated.class);
        startActivity(intent);
        finish();
    }
}

