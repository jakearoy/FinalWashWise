package com.example.afinal;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CourierMatcher {

    private DatabaseReference mDatabase;
    private FirebaseDBHandler dbHandler;
    private GeoApiContext mGeoApiContext;
    ArrayList<Courier> couriers;

    public CourierMatcher() {
        dbHandler = new FirebaseDBHandler();

        // Initialize the Google Maps API context
        mGeoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyCW2jyHkjPiRtBpuJN-ZLNBsuJGocUuPYE")
                .build();

    }


    public void getDistance(ArrayList<Courier> couriers, Order order) {


    }

    public void calculateDistances(Order order, ArrayList<Courier> couriers, OnDistancesCalculatedListener listener) throws InterruptedException, ApiException, IOException {
        // Create a list to hold the distances
        ArrayList<Double> distances = new ArrayList<>();
        String originAddress = order.getUserAddress();

        ArrayList<String> destinationAddresses = new ArrayList<>();

        // Add each courier's address to the destinationAddresses list
        for (Courier courier : couriers) {
            System.out.println("courier object" + courier);
            destinationAddresses.add((String) courier.getAddress());
            System.out.println("destination addrsses array" + destinationAddresses);
        }

        // Build the request for each destination address
        for (String destinationAddress : destinationAddresses) {
            DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(mGeoApiContext)
                    .origins(originAddress)
                    .destinations(destinationAddress)
                    .mode(TravelMode.DRIVING)
                    .units(Unit.IMPERIAL);

            try {
                DistanceMatrix result = request.await();
                Distance distance = result.rows[0].elements[0].distance;

                double distanceInMeters = distance.inMeters;

                double distanceInMiles = distanceInMeters * 0.000621371192;

                distances.add(distanceInMiles);
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Sort the list of Couriers by distance
        Collections.sort(couriers, new Comparator<Courier>() {
            @Override
            public int compare(Courier c1, Courier c2) {
                System.out.println("couriers array after comparison: " + couriers);
                return Double.compare(c1.getDistance(), c2.getDistance());

            }
        });

        if (couriers.size() > 1) {
            couriers.remove(1);
        }

        // Set the assignedCourier attribute of your Order object to the closest courier
        if (!couriers.isEmpty()) {
            order.setAssignedCourier(couriers.get(0));
            System.out.println("Order assigned: " + order.getAssignedCourier());
            Courier assignedCourier = order.getAssignedCourier();
            String courierEmail = assignedCourier.getEmail().toLowerCase();

            order.setAssignedCourier(assignedCourier);
            order.setCourierEmail(courierEmail);

            System.out.println("courier email" + order.getCourierEmail());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Orders");

            String orderId = order.getOrderId();

            myRef.child(orderId).setValue(order);

        }
    }

    public void calculateDistances(Order order, List<Courier> couriers) {
    }

    public interface OnDistancesCalculatedListener {
        void onDistancesCalculated();
    }


    public void matchCouriersToOrder(Order order, OnCouriersMatchedListener listener) {
        // Retrieve all of the Couriers from the database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Couriers");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Courier courier = dataSnapshot.getValue(Courier.class);
                    couriers.add(courier);
                }
                List<Courier> matchedCouriers = findMatchedCouriers(couriers, order);
                listener.onCouriersMatched(matchedCouriers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    public List<Courier> findMatchedCouriers(List<Courier> couriers, Order order) {

        List<Courier> matchedCouriers = new ArrayList<>();
        return matchedCouriers;
    }


    public interface OnCouriersMatchedListener {
        void onCouriersMatched(List<Courier> couriers);
    }


    public interface OnCouriersRetrievedListener {
        void onCouriersRetrieved(ArrayList<Courier> couriers);
    }


    // Retrieve the Couriers from the database
    public void retrieveCouriersFromDatabase(OnCouriersRetrievedListener listener) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Couriers");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Courier> couriers = new ArrayList<>();

                for (DataSnapshot courierSnapshot : dataSnapshot.getChildren()) {
                    Courier courier = courierSnapshot.getValue(Courier.class);
                    couriers.add(courier);
                }
                listener.onCouriersRetrieved(couriers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void printCouriers() {
        retrieveCouriersFromDatabase(new OnCouriersRetrievedListener() {
            @Override
            public void onCouriersRetrieved(ArrayList<Courier> couriers) {
                System.out.println(couriers);
            }
        });
    }
}