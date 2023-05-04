package Payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.afinal.Courier;
import com.example.afinal.CourierMatcher;
import com.example.afinal.FirebaseDBHandler;
import com.example.afinal.HomeScreen;
import com.example.afinal.Laundromat;
import com.example.afinal.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.stripe.android.PaymentConfiguration;

import com.example.afinal.R;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Checkout extends AppCompatActivity {

    private Laundromat selectedLaundromat;
    String address;
    Button btn;
    String SECRET_KEY = "sk_test_51N1UYDHErql9TKUSRg0Fa2mDiABtgVnFHjLoLmjVO1T1crYpsBB3XhEiEyJ4dKGH4ftirN9BGZTX5blBxgPy1RN900V7hDZ8XC";
    String PUBLISH_KEY = "pk_test_51N1UYDHErql9TKUSzQ1VwTXIgiJp8kKoyt5xJArKIWiDL2NgIhPP1wKWuhiKphqxRyXnFNbhCu1CTDpGE2IeCReT00R6MbG48b";
    private int wash_quantity, iron_quantity, dryclean_quantity, washIron_quantity;
    PaymentSheet paymentsheet;
    private DatabaseReference mDatabase;
    private GeoApiContext mGeoApiContext;
    String customerID;
    String EphericalKey;
    String ClientSecret;
    String deliveryDate, collectionDate;
    double total;
    double totalCostValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


        // Initialize the Google Maps API context
        mGeoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyCW2jyHkjPiRtBpuJN-ZLNBsuJGocUuPYE")
                .build();



        collectionDate = getIntent().getStringExtra("collection_date");
        deliveryDate = getIntent().getStringExtra("delivery_date");

        selectedLaundromat = getIntent().getParcelableExtra("selectedLaundromat");

        address = getIntent().getStringExtra("address");

        total = getIntent().getDoubleExtra("total", 0);

        wash_quantity = getIntent().getIntExtra("WASH_QUANTITY", 0);

        washIron_quantity = getIntent().getIntExtra("WASH_IRON_QUANTITY", 0);

        dryclean_quantity = getIntent().getIntExtra("DRY_QUANTITY", 0);

        iron_quantity = getIntent().getIntExtra("IRON_QUANTITY", 0);

        TextView serviceText = findViewById(R.id.checkout_service);

        TextView servicePrice = findViewById(R.id.service_price);

        TextView totalCost = findViewById(R.id.total_cost);

        System.out.println(selectedLaundromat.getName());
        System.out.println(address);
        System.out.println(total);
        System.out.println(collectionDate);
        System.out.println(deliveryDate);


        if (wash_quantity != 0) {
            serviceText.setText("Service: " + wash_quantity + " X Wash");

        } else if (iron_quantity != 0) {
            serviceText.setText("Service: " + iron_quantity + " X Iron");

        } else if (dryclean_quantity != 0) {
            serviceText.setText("Service: " + dryclean_quantity + " X Dry Clean");

        } else if (washIron_quantity != 0) {
            serviceText.setText("Service: " + washIron_quantity + " X Wash And Iron");

        }

        //calculate Total cost inc. delivery

        String formattedTotal = String.format("%.2f", total);

        servicePrice.setText("Service Price: £" + formattedTotal);

       totalCostValue = total + (1.99 + 1.99);

       String formattedTotalCost = String.format("%.2f", totalCostValue);

       totalCost.setText("Total Cost: £"+formattedTotalCost);

        btn = findViewById(R.id.pay_btn);

        PaymentConfiguration.init(this, PUBLISH_KEY);


        paymentsheet = new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);

        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PaymentFlow();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            customerID = object.getString("id");

                            getEphericalKey(customerID);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);


    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {

        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();
        }


    }

    private void getEphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("id");



                            getClientSecret(customerID, EphericalKey);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);

    }

    private void getClientSecret(String customerID, String ephericalKey) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            ClientSecret = object.getString("client_secret");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", String.valueOf((int) (totalCostValue * 100)));
                params.put("currency", "gbp");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Checkout.this);
        requestQueue.add(stringRequest);
    }

    private void PaymentFlow() throws InterruptedException, ApiException, IOException {

        paymentsheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration("WashWise"
                        , new PaymentSheet.CustomerConfiguration(
                        customerID,
                        EphericalKey
                ))
        );

        // If the payment was successful, create and store the order
        Order order = new Order(collectionDate, deliveryDate, total, address, selectedLaundromat);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Orders");
        String orderId = myRef.push().getKey();

        // Assign orderId to the order
        order.setOrderId(orderId);


        // Store the order using the generated key
        myRef.child(orderId).setValue(order);



        // Match couriers to the order and calculate distances
        ArrayList<Courier> courierArrayList = new ArrayList<>();

        CourierMatcher matcher = new CourierMatcher();

        CourierMatcher.OnCouriersRetrievedListener listener;
        mDatabase = FirebaseDatabase.getInstance().getReference("Couriers");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot courierSnapshot : dataSnapshot.getChildren()) {
                    Courier courier = courierSnapshot.getValue(Courier.class);
                    courierArrayList.add(courier);
                }
                try {
                    matcher.calculateDistances(order, courierArrayList, new CourierMatcher.OnDistancesCalculatedListener() {
                        @Override
                        public void onDistancesCalculated() {
                                System.out.println("all couriers" + courierArrayList);
                                System.out.println("Order assigned to " + order.getAssignedCourier());

                                Courier assignedCourier = order.getAssignedCourier();
                                String courierEmail = assignedCourier.getEmail();

                                order.setAssignedCourier(assignedCourier);
                                order.setCourierEmail(courierEmail);

                                System.out.println("courier email" + order.getCourierEmail());
                                myRef.child(orderId).setValue(order); //saves updated order value with courierEmail
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    public void finishPayment(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
        finish();
    }
}


















