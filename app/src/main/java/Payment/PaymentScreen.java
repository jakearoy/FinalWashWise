package Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.afinal.HomeScreen;
import com.example.afinal.Laundromat;
import com.example.afinal.R;

public class PaymentScreen extends AppCompatActivity {
    private Laundromat selectedLaundromat;
    private int wash_quantity, iron_quantity, dryclean_quantity, washIron_quantity;
    Button checkoutBtn;
    double total;
    String address;
    String deliveryDate, collectionDate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);

        checkoutBtn = findViewById(R.id.checkout_btn);

        selectedLaundromat = getIntent().getParcelableExtra("selectedLaundromat");

        address = getIntent().getStringExtra("address");

        wash_quantity = getIntent().getIntExtra("WASH_QUANTITY",0);

        washIron_quantity = getIntent().getIntExtra("WASH_IRON_QUANTITY",0);

        dryclean_quantity = getIntent().getIntExtra("DRY_QUANTITY",0);

        iron_quantity = getIntent().getIntExtra("IRON_QUANTITY",0);

        Intent intent = getIntent();
        selectedLaundromat = getIntent().getParcelableExtra("selectedLaundromat");



        collectionDate = intent.getStringExtra("collectionDate");
        deliveryDate = intent.getStringExtra("deliveryDate");
        String address = intent.getStringExtra("address");

      TextView priceBreakdown = findViewById(R.id.price_breakdown);


        //Determine which text to display according to what type of service is being used

        if (wash_quantity != 0 ) {
             total = wash_quantity * selectedLaundromat.getWashPrice();
            String formattedTotal = String.format("%.2f", total);
            priceBreakdown.setText(wash_quantity + "X Wash, Total (excluding delivery): £" + formattedTotal);
        } else if (iron_quantity != 0) {
             total = iron_quantity * selectedLaundromat.getIronPrice();
            String formattedTotal = String.format("%.2f", total);
            priceBreakdown.setText(iron_quantity + "X Iron, Total (excluding delivery): £" + formattedTotal);
        } else if (dryclean_quantity != 0 ) {
             total = dryclean_quantity * selectedLaundromat.getDryCleanPrice();
            String formattedTotal = String.format("%.2f", total);
            priceBreakdown.setText(dryclean_quantity + "X Dry-Clean, Total (excluding delivery): £" + formattedTotal);
        } else if (washIron_quantity != 0){
             total = washIron_quantity * selectedLaundromat.getWashIronPrice();
            String formattedTotal = String.format("%.2f", total);
            priceBreakdown.setText(washIron_quantity + " X Wash and Iron, Total (excluding delivery): £" + formattedTotal);
        }





        TextView selectedLaundryName = findViewById(R.id.selected_laundry_name);
        selectedLaundryName.setText(selectedLaundromat.getName());

        TextView selectedLaundryNumber = findViewById(R.id.selected_laundry_number);
        selectedLaundryNumber.setText(selectedLaundromat.getNumber());

        TextView selectedLaundryDistance = findViewById(R.id.selected_laundry_distance);
        selectedLaundryDistance.setText(String.format("%.2f", selectedLaundromat.getDistance()) + " Miles away");

        TextView collectionAddy = findViewById(R.id.collection_address);
        collectionAddy.setText("Collection from " + address);

        TextView deliveryAddy = findViewById(R.id.delivery_address);
        deliveryAddy.setText("Delivery to " + address);

        TextView collectionDateTV = findViewById(R.id.collection_date);
        collectionDateTV.setText("Date of collection: " + collectionDate);

        TextView deliveryDateTV = findViewById(R.id.delivery_date);
        deliveryDateTV.setText("Date of delivery: " + deliveryDate);





        //pass details of payment to checkout screen
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(total);
                Intent intent = new Intent(getApplicationContext(), Checkout.class);
                intent.putExtra("total", total);
                intent.putExtra("WASH_QUANTITY", wash_quantity);
                intent.putExtra("WASH_IRON_QUANTITY",washIron_quantity);
                intent.putExtra("DRY_QUANTITY", dryclean_quantity);
                intent.putExtra("address", address);
                intent.putExtra("collection_date", collectionDate);
                intent.putExtra("delivery_date", deliveryDate);
                intent.putExtra("selectedLaundromat", selectedLaundromat);
                intent.putExtra("IRON_QUANTITY", iron_quantity);
                startActivity(intent);
                finish();
            }
        });

    }

    public void finishPayment(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
        finish();
    }
}