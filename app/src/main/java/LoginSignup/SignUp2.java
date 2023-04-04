package LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.afinal.R;

import java.util.Calendar;

public class SignUp2 extends AppCompatActivity {
    ImageView backBtn;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        backBtn = findViewById(R.id.signUp_back_arrow2);
        datePicker = findViewById(R.id.age_picker);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void callNextSignUpScreen(View view){

        if (!validateAge()){
            return;
        }

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();


        Intent intentFromPrevActivity = getIntent();
        String fullName = intentFromPrevActivity.getStringExtra("FullName");
        String username = intentFromPrevActivity.getStringExtra("Username");
        String email = intentFromPrevActivity.getStringExtra("Email");
        String password = intentFromPrevActivity.getStringExtra("Password");

        Intent intent = new Intent(getApplicationContext(), SignUp3.class);
        intent.putExtra("Age",datePicker.getYear());
        intent.putExtra("FullName", fullName);
        intent.putExtra("Password", password);
        intent.putExtra("Email", email);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    private boolean validateAge(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;
        if (isAgeValid < 16) {
            Toast.makeText(this, "You must be atleast 16 years of age to create an account", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

}