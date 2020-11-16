package com.example.vijaya.myorder;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderSummaryActivity extends AppCompatActivity {

    private TextView orderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        Bundle bundles = getIntent().getExtras();
        String orderParagraph = bundles.getString("orderSummaryKey");

        orderDetails = (TextView) findViewById(R.id.order_paragraph);
        orderDetails.setText(orderParagraph);

        Button returnHome = findViewById(R.id.return_to_order);
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
