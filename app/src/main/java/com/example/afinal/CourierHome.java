package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CourierHome extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Order assignedOrder;
    private List<Order> assignedOrders;
    TextView currentOrderAddy;
    TextView currentOrderDelivery;
    TextView currentOrderCollection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_home);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String courierEmail = currentUser.getEmail().toLowerCase();
        Log.d("CourierHomeActivity", "Current user's email: " + courierEmail);

        currentOrderAddy = findViewById(R.id.current_order_addy);
        currentOrderDelivery = findViewById(R.id.current_order_delivery);
        currentOrderCollection = findViewById(R.id.current_order_collection);
        Button completeBtn = findViewById(R.id.complete_btn);

        completeBtn.setVisibility(View.GONE);

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        Query assignedOrdersQuery = ordersRef.orderByChild("courierEmail").equalTo(courierEmail);
        assignedOrdersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    assignedOrders = new ArrayList<>();

                    // Retrieve the first matching order
                    assignedOrder = dataSnapshot.getChildren().iterator().next().getValue(Order.class);


                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);

                        // Check if the order status is "COMPLETED"
                        if (order.getStatus().toString() == "COMPLETED") {
                            Log.d("CourierHomeActivity", "Order already completed: " + order.getOrderId());
                            continue; // Skip this order and move to the next one
                        }

                        assignedOrders.add(order);
                    }
                    if (!assignedOrders.isEmpty()) {
                        // Display the first assigned order
                        assignedOrder = assignedOrders.get(0);
                        Log.d("CourierHomeActivity", "Assigned order: " + assignedOrder);
                        completeBtn.setVisibility(View.VISIBLE);

                        currentOrderAddy.setText("User address: " + assignedOrder.getUserAddress());
                        currentOrderDelivery.setText("Delivery Date: " + assignedOrder.getDeliveryDate());
                        currentOrderCollection.setText("Collection Date: " + assignedOrder.getCollectionDate());
                    } else {
                        Log.d("CourierHomeActivity", "No matching orders found for courier email: " + courierEmail);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

    }

    public void completeOrder(View view) {
        if (assignedOrder == null) {
            return;
        }
        String orderId = assignedOrder.getOrderId();
        assignedOrder.setStatus(Order.Status.COMPLETED);
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        orderRef.child("status").setValue(Order.Status.COMPLETED);
        System.out.println(assignedOrder.getStatus());
        Toast.makeText(CourierHome.this, "Order Completed", Toast.LENGTH_SHORT).show();

        // Remove the completed order from the list of assigned orders
        assignedOrders.remove(assignedOrder);

        if (!assignedOrders.isEmpty()) {
            // Display the next assigned order
            assignedOrder = assignedOrders.get(0);
            Log.d("CourierHomeActivity", "Assigned order: " + assignedOrder);

            currentOrderAddy.setText("User address: " + assignedOrder.getUserAddress());
            currentOrderDelivery.setText("Delivery Date: " + assignedOrder.getDeliveryDate());
            currentOrderCollection.setText("Collection Date: " + assignedOrder.getCollectionDate());
        }
    }
}