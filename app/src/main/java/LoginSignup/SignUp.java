package LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.afinal.R;
import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity {
    ImageView backBtn;
    TextInputLayout fullName, Username, Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        backBtn = findViewById(R.id.signUp_back_arrow);

        fullName = findViewById(R.id.signUpFullName);
        Username = findViewById(R.id.signUpUsername);
        Email = findViewById(R.id.signUpEmail);
        Password = findViewById(R.id.signUpPassword);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Welcome.class);
                startActivity(intent);
                finish();
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

    private boolean validateUsername() {
        String val = Username.getEditText().getText().toString().trim();
        String checkspace = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            Username.setError("Field can not be empty");
            return false;
        } else if (val.length() > 20) {
            Username.setError("Username is too long!");
            return false;
        } else if (!val.matches(checkspace)) {
            Username.setError("Username can not have whitespaces");
            return false;
        } else {
            Username.setError(null);
            Username.setErrorEnabled(false);
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

        if (!validateFullName() | !validateUsername() | !validateEmail() | !validatePassword()){
            return;
        }

        Intent intent = new Intent(getApplicationContext(), SignUp2.class);
        intent.putExtra("FullName", fullName.toString());
        intent.putExtra("Username", Username.toString());
        intent.putExtra("Email", Email.toString());
        intent.putExtra("Password", Password.toString());

        startActivity(intent);


    }

}