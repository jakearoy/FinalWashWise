package com.example.afinal;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteFragment;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;


import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class deliveryScreen extends AppCompatActivity {

    String address;
    private DatabaseReference mDatabase;
    public AutocompleteSupportFragment autocompleteFragment;
    private GeoApiContext mGeoApiContext;
    private Laundromat selectedLaundromat;
    private int wash_quantity;
    private int iron_quantity;
    private int dryclean_quantity;
    private int washIron_quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_screen);

        //retrieve quantity

        wash_quantity = getIntent().getIntExtra("WASH_QUANTITY", 0);
        System.out.println(wash_quantity);


        washIron_quantity = getIntent().getIntExtra("WASH_IRON_QUANTITY", 0);
        System.out.println(washIron_quantity);


        dryclean_quantity = getIntent().getIntExtra("DRY_QUANTITY", 0);
        System.out.println(dryclean_quantity);


        iron_quantity = getIntent().getIntExtra("IRON_QUANTITY", 0);
        System.out.println(iron_quantity);

        // Initialize the Google Maps API context
        mGeoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyCW2jyHkjPiRtBpuJN-ZLNBsuJGocUuPYE")
                .build();


        // Set visibility of cardviews
        CardView CV1 = findViewById(R.id.laundry_suggestion1);
        CardView CV2 = findViewById(R.id.laundry_suggestion2);
        CardView CV3 = findViewById(R.id.laundry_suggestion3);

        CV1.setVisibility(View.GONE);
        CV2.setVisibility(View.GONE);
        CV3.setVisibility(View.GONE);

        TextView select_laundromat = findViewById(R.id.select_laundromat_text);

        select_laundromat.setVisibility(View.GONE);

        // Set up the autocomplete fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        autocompleteFragment = new AutocompleteSupportFragment();

        fragmentTransaction.replace(R.id.autocomplete_fragment_container, autocompleteFragment);
        fragmentTransaction.commit();

        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
        autocompleteFragment.setCountry("UK"); // Replace with your country code
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Handle the selected place
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                address = place.getAddress();
                getDistance();
            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle the error
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // Set up the checkbox listener
        CheckBox checkBox = findViewById(R.id.check_box);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDatabase = FirebaseDatabase.getInstance().getReference()
                            .child("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("address");

                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String address = snapshot.getValue(String.class);
                            if (address != null) {
                                autocompleteFragment.setText(address);
                                deliveryScreen.this.address = address;
                                getDistance();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
                } else {
                    // Checkbox is unchecked, perform action
                    return;
                }
            }
        });
    }

    public void getDistance() {
        // Get the origin and destination addresses
        String originAddress = address != null ? address : "";


        // Return early if the origin address is null
        if (originAddress.isEmpty()) {
            return;
        }

        // Retrieve all of the laundromats' addresses from the database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("laundromats");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> destinationAddresses = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String destinationAddress = dataSnapshot.child("address").getValue(String.class);
                    if (destinationAddress != null) {
                        destinationAddresses.add(destinationAddress);
                    }
                }
                calculateDistances(originAddress, destinationAddresses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void calculateDistances(String originAddress, ArrayList<String> destinationAddresses) {
        // Create a list to hold the distances and laundromats
        ArrayList<Double> distances = new ArrayList<>();
        ArrayList<Laundromat> laundromats = new ArrayList<>();


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

                // Retrieve the laundromat information from the database
                mDatabase = FirebaseDatabase.getInstance().getReference().child("laundromats");
                Query query = mDatabase.orderByChild("address").equalTo(destinationAddress);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Laundromat laundromat = new Laundromat(
                                        dataSnapshot.child("name").getValue(String.class),
                                        dataSnapshot.child("address").getValue(String.class),
                                        dataSnapshot.child("washPrice").getValue(Double.class),
                                        dataSnapshot.child("ironPrice").getValue(Double.class),
                                        dataSnapshot.child("dryCleanPrice").getValue(Double.class),
                                        dataSnapshot.child("number").getValue(String.class),
                                        dataSnapshot.child("washIronPrice").getValue(Double.class)

                                );
                                laundromat.setDistance(distanceInMiles);
                                laundromats.add(laundromat);

                                // Sort the list of laundromats by distance
                                Collections.sort(laundromats, new Comparator<Laundromat>() {
                                    @Override
                                    public int compare(Laundromat l1, Laundromat l2) {
                                        return Double.compare(l1.getDistance(), l2.getDistance());
                                    }
                                });

                                // Only keep the three closest laundromats
                                if (laundromats.size() > 3) {
                                    laundromats.remove(3);
                                }

                                // Display the laundromat details in the UI
                                String nameText1 = "";
                                String nameText2 = "";
                                String nameText3 = "";

                                String addyText1 = "";
                                String addyText2 = "";
                                String addyText3 = "";

                                String distanceText1 = "";
                                String distanceText2 = "";
                                String distanceText3 = "";


                                String distanceText = "";

                                for (int i = 0; i < laundromats.size(); i++) {
                                    Laundromat l = laundromats.get(i);
                                    String formattedDistance = String.format("%.2f", l.getDistance());
                                    if (i % 3 == 0) {
                                        nameText1 += l.getName() + "\n";
                                        addyText1 += l.getAddress() + "\n";
                                        distanceText1 += "Distance: " + formattedDistance + " miles\n\n";
                                    } else if (i % 3 == 1) {
                                        nameText2 += l.getName() + "\n";
                                        addyText2 += l.getAddress() + "\n";
                                        distanceText2 += "Distance: " + formattedDistance + " miles\n\n";
                                    } else {
                                        nameText3 += l.getName() + "\n";
                                        addyText3 += l.getAddress() + "\n";
                                        distanceText3 += "Distance: " + formattedDistance + " miles\n\n";
                                    }

                                    TextView laundryTV1 = findViewById(R.id.laundry1_name);
                                    TextView laundryTV2 = findViewById(R.id.laundry2_name);
                                    TextView laundryTV3 = findViewById(R.id.laundry3_name);
                                    TextView distanceTV1 = findViewById(R.id.laundry1_distance);
                                    TextView distanceTV2 = findViewById(R.id.laundry2_distance);
                                    TextView distanceTV3 = findViewById(R.id.laundry3_distance);
                                    TextView addyTV1 = findViewById(R.id.laundry1_address);
                                    TextView addyTV2 = findViewById(R.id.laundry2_address);
                                    TextView addyTV3 = findViewById(R.id.laundry3_address);

                                    // Set visibility of cardviews
                                    CardView CV1 = findViewById(R.id.laundry_suggestion1);
                                    CardView CV2 = findViewById(R.id.laundry_suggestion2);
                                    CardView CV3 = findViewById(R.id.laundry_suggestion3);

                                    CV1.setVisibility(View.VISIBLE);
                                    CV2.setVisibility(View.VISIBLE);
                                    CV3.setVisibility(View.VISIBLE);

                                    TextView select_laundromat = findViewById(R.id.select_laundromat_text);
                                    select_laundromat.setVisibility(View.VISIBLE);

                                    laundryTV1.setText(nameText1);
                                    laundryTV2.setText(nameText2);
                                    laundryTV3.setText(nameText3);


                                    distanceTV1.setText(distanceText1);
                                    distanceTV2.setText(distanceText2);
                                    distanceTV3.setText(distanceText3);


                                    addyTV1.setText(addyText1);
                                    addyTV2.setText(addyText2);
                                    addyTV3.setText(addyText3);


                                    TextView distanceTextView = findViewById(R.id.distance_text_view);
                                    distanceTextView.setText(distanceText);

                                    final CardView[] cardViews = {CV1, CV2, CV3};
                                    final CardView[] previouslyClicked = {null};

                                    for (int y = 0; y < cardViews.length; y++) {
                                        final int index = y;
                                        cardViews[y].setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Unhighlight previously clicked card view
                                                if (previouslyClicked[0] != null) {
                                                    previouslyClicked[0].setCardBackgroundColor(Color.WHITE);
                                                }
                                                // Highlight clicked card view
                                                cardViews[index].setCardBackgroundColor(Color.LTGRAY);
                                                previouslyClicked[0] = cardViews[index];

                                                selectedLaundromat = laundromats.get(index);

                                            }
                                        });
                                    }


                                }
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void callCancel(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
        startActivity(intent);
        finish();
    }

    public void callNextDeliveryScreen(View view) {
        if (selectedLaundromat != null) {
            System.out.println("before passing" + iron_quantity);
            Intent intent = new Intent(getApplicationContext(), deliveryScreen2.class);
            intent.putExtra("selectedLaundromat", selectedLaundromat);
            intent.putExtra("WASH_QUANTITY", wash_quantity);
            intent.putExtra("WASH_IRON_QUANTITY", washIron_quantity);
            intent.putExtra("DRY_QUANTITY", dryclean_quantity);
            intent.putExtra("IRON_QUANTITY", iron_quantity);
            intent.putExtra("address", address);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please select a laundromat", Toast.LENGTH_SHORT).show();
        }
    }
}