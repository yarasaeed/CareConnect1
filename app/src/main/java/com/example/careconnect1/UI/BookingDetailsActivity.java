package com.example.careconnect1.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.careconnect1.R;

public class BookingDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_details_layout);

        // Initialize views
        TextView paymentDetailsTextView = findViewById(R.id.payment_details);

        // Retrieve booking details (Example: from intent extras)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String paymentDetails = extras.getString("payment_details_key");

            // Populate views
            paymentDetailsTextView.setText(paymentDetails);
        }
    }
}
