package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

import Payment.PaymentScreen;

public class deliveryScreen2 extends AppCompatActivity {

    private DatePickerDialog datePickerDialog1, datePickerDialog2;
    private TextView dateText1, dateText2;
    int hour, minute;
    private Laundromat selectedLaundromat;
    private String address;
    private int wash_quantity;
    private int iron_quantity;
    private int dryclean_quantity;
    private int washIron_quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_screen2);


        wash_quantity = getIntent().getIntExtra("WASH_QUANTITY", 0);
        System.out.println(wash_quantity);

        washIron_quantity = getIntent().getIntExtra("WASH_IRON_QUANTITY", 0);
        System.out.println(washIron_quantity);

        dryclean_quantity = getIntent().getIntExtra("DRY_QUANTITY", 0);
        System.out.println(dryclean_quantity);


        iron_quantity = getIntent().getIntExtra("IRON_QUANTITY", 0);
        System.out.println(iron_quantity);

        initDatePicker1();
        initDatePicker2();
        dateText1 = findViewById(R.id.date_label);
        dateText1.setText(getTodaysDate());

        dateText2 = findViewById(R.id.deliver_date_label);
        dateText2.setText(getTodaysDate());


        selectedLaundromat = getIntent().getParcelableExtra("selectedLaundromat");
        System.out.println(selectedLaundromat);

        address = getIntent().getStringExtra("address");
        TextView selectedLaundromatName = findViewById(R.id.selected_laundromat_title);
        selectedLaundromatName.setText(selectedLaundromat.getName());

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1; //plus 1 to get correct value
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker1() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                dateText1.setText(date);

            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog1 = new DatePickerDialog(this, style, dateSetListener, year, month, day);

        //get rid of year spinner in datePicker
        datePickerDialog1.getDatePicker().findViewById(Resources.getSystem().getIdentifier("year", "id", "android")).setVisibility(View.GONE);


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long minDate = calendar.getTimeInMillis();
        datePickerDialog1.getDatePicker().setMinDate(minDate);



    }

    private void initDatePicker2() {
        DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                dateText2.setText(date);

            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog2 = new DatePickerDialog(this, style, dateSetListener2, year, month, day);

        //get rid of year spinner in datePicker

        datePickerDialog2.getDatePicker().findViewById(Resources.getSystem().getIdentifier("year", "id", "android")).setVisibility(View.GONE);


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long minDate = calendar.getTimeInMillis();
        datePickerDialog2.getDatePicker().setMinDate(minDate);



    }


    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + dayOfMonth;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUNE";
        if (month == 7)
            return "JULY";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //return as default (should never happen)
        return "JAN";
    }

    public void openDatePicker1(View view) {
        datePickerDialog1.show();
    }

    public void openDatePicker2(View view) {
        datePickerDialog2.show();
    }


    int style = AlertDialog.THEME_HOLO_LIGHT;


    public void openPaymentScreen(View view) {
        if (selectedLaundromat != null) {
            Intent intent = new Intent(getApplicationContext(), PaymentScreen.class);
            intent.putExtra("selectedLaundromat", selectedLaundromat);
            intent.putExtra("collectionDate", dateText1.getText());
            intent.putExtra("WASH_QUANTITY", wash_quantity);
            intent.putExtra("WASH_IRON_QUANTITY", washIron_quantity);
            intent.putExtra("DRY_QUANTITY", dryclean_quantity);
            intent.putExtra("IRON_QUANTITY", iron_quantity);
            intent.putExtra("deliveryDate", dateText2.getText());
            intent.putExtra("address", address);
            intent.putExtra("washQuantity", wash_quantity);
            System.out.println(wash_quantity);
            startActivity(intent);
        }
    }
}