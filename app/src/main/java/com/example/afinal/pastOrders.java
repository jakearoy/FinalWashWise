package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import LoginSignup.Login;

public class pastOrders extends AppCompatActivity {

    User user;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_orders);


        TextView orderLaundromat = findViewById(R.id.order_laundromat);
        TextView orderPrice = findViewById(R.id.order_price);
        TextView orderDelivery = findViewById(R.id.order_delivery);
        TextView orderCollection = findViewById(R.id.order_collection);
        TextView orderStatus = findViewById(R.id.order_status);

        backBtn = findViewById(R.id.back_arrow);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users").child(userId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Create new User object from the dataSnapshot
                    user = dataSnapshot.getValue(User.class);
                    Log.d("TAG", "User full name: " + user.getFullName());
                    Log.d("TAG", "User email: " + user.getEmail());
                    Log.d("TAG", "User address: " + user.getAddress());
                    Log.d("TAG", "User phone number: " + user.getPhoneNo());

                    String userAddress = user.getAddress();
                    DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
                    Query addressQuery = ordersRef.orderByChild("userAddress").equalTo(userAddress);

                    addressQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {

                                Order order = new Order();

                                order.setOrderId(orderSnapshot.getKey()); // set the order ID
                                order.setCollectionDate(orderSnapshot.child("collectionDate").getValue(String.class)); // set collection date
                                order.setDeliveryDate(orderSnapshot.child("deliveryDate").getValue(String.class)); // set delivery date
                                order.setPrice(orderSnapshot.child("price").getValue(Double.class)); // set price
                                order.setUserAddress(orderSnapshot.child("userAddress").getValue(String.class)); // set user address
                                order.setStatus(orderSnapshot.child("status").getValue(Order.Status.class)); // set order status

                                System.out.println(order);
                                System.out.println(order.getCollectionDate());
                                System.out.println(order.getPrice());

                                //set corresponding text views

                                orderPrice.setText("Price: £" + String.valueOf((double) order.getPrice()));
                                orderDelivery.setText("Delivery date: " + order.getDeliveryDate());
                                orderCollection.setText("Collection date: " + order.getCollectionDate());
                                orderStatus.setText("Order status: " + order.getStatus().toString());


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    Log.d("TAG", "User not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Error reading user data: " + databaseError.getMessage());
            }
        });

    }
}