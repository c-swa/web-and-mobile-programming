package com.example.vijaya.myorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String MAIN_ACTIVITY_TAG = "MainActivity";
    final int PIZZA_PRICE = 5;
    final int PEPPERONI_PRICE = 1;
    final int ITALIAN_SAUSAGE_PRICE = 2;
    final int MUSHROOM_PRICE = 1;
    final int BLACK_OLIVES_PRICE = 2;
    final int GREEN_PEPPERS_PRICE = 1;
    final int PINEAPPLES_PRICE = 2;

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button placeOrder = (Button) findViewById(R.id.order_button);
        Button viewSummary = (Button) findViewById(R.id.summary_button);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder(v);
                // By pressing the button, executes the code inside submitOrder to process the order.
            }
        });

        viewSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summarizeOrder(v);
            }
        });
    }

    /**
     * When the summary_button is pressed, this method is called
     */
    public void summarizeOrder(View view){
        // get user's desired order name
        EditText userOrderNameView = (EditText) findViewById(R.id.order_name);
        String userOrderName = userOrderNameView.getText().toString();

        // check if pepperoni is selected
        CheckBox pepperoni = (CheckBox) findViewById(R.id.pepperoni_checked);
        boolean hasPepperoni = pepperoni.isChecked();

        // check if italian sausage is selected
        CheckBox italianSausage = (CheckBox) findViewById(R.id.italian_sausage_checked);
        boolean hasItalianSausage = italianSausage.isChecked();

        CheckBox mushrooms = (CheckBox) findViewById(R.id.mushrooms_checked);
        boolean hasMushrooms = mushrooms.isChecked();

        CheckBox blackOlives = (CheckBox) findViewById(R.id.black_olives_checked);
        boolean hasBlackOlives = blackOlives.isChecked();

        CheckBox greenPeppers = (CheckBox) findViewById(R.id.green_peppers_checked);
        boolean hasGreenPeppers = greenPeppers.isChecked();

        CheckBox pineapples = (CheckBox) findViewById(R.id.pineapples_checked);
        boolean hasPineapples = pineapples.isChecked();

        // calculate and store the total price
        float totalPrice = calculatePrice(hasPepperoni, hasBlackOlives, hasGreenPeppers, hasItalianSausage, hasMushrooms, hasPineapples);

        // create and store the order summary
        String orderSummaryMessage = createOrderSummary(userOrderName,hasPepperoni,hasItalianSausage,hasMushrooms,hasBlackOlives,hasGreenPeppers,hasPineapples,totalPrice);

        Intent viewSummary = new Intent(this.getApplicationContext(), OrderSummaryActivity.class);
        viewSummary.putExtra("orderSummaryKey", orderSummaryMessage);
        startActivity(viewSummary);
    }


    /**
     * When the order button is pressed, this method is called.
     */
    public void submitOrder(View view) {

        // get user's desired order name
        EditText userOrderNameView = (EditText) findViewById(R.id.order_name);
        String userOrderName = userOrderNameView.getText().toString();

        // check if pepperoni is selected
        CheckBox pepperoni = (CheckBox) findViewById(R.id.pepperoni_checked);
        boolean hasPepperoni = pepperoni.isChecked();

        // check if italian sausage is selected
        CheckBox italianSausage = (CheckBox) findViewById(R.id.italian_sausage_checked);
        boolean hasItalianSausage = italianSausage.isChecked();

        CheckBox mushrooms = (CheckBox) findViewById(R.id.mushrooms_checked);
        boolean hasMushrooms = mushrooms.isChecked();

        CheckBox blackOlives = (CheckBox) findViewById(R.id.black_olives_checked);
        boolean hasBlackOlives = blackOlives.isChecked();

        CheckBox greenPeppers = (CheckBox) findViewById(R.id.green_peppers_checked);
        boolean hasGreenPeppers = greenPeppers.isChecked();

        CheckBox pineapples = (CheckBox) findViewById(R.id.pineapples_checked);
        boolean hasPineapples = pineapples.isChecked();

        // calculate and store the total price
        float totalPrice = calculatePrice(hasPepperoni, hasBlackOlives, hasGreenPeppers, hasItalianSausage, hasMushrooms, hasPineapples);

        // create and store the order summary
        String orderSummaryMessage = createOrderSummary(userOrderName,hasPepperoni,hasItalianSausage,hasMushrooms,hasBlackOlives,hasGreenPeppers,hasPineapples,totalPrice);

        // Sends an email with the Order Name, and Order Summary Contents.
        sendEmail(userOrderName,orderSummaryMessage);

    }

    public void sendEmail(String name, String output) {
        // Write the relevant code for triggering email

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"sample@example.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Order for: " + name);
        emailIntent.putExtra(Intent.EXTRA_TEXT, output);
        startActivity(emailIntent);

    }

    private String boolToString(boolean bool) {
        return bool ? (getString(R.string.yes)) : (getString(R.string.no));
    }

    private String createOrderSummary(String userInputName, boolean hasPepperoni, boolean hasItalianSausage,
                                      boolean hasMushrooms, boolean hasBlackOlives, boolean hasGreenPeppers,
                                      boolean hasPineapples, float price) {
        String orderSummaryMessage = getString(R.string.order_summary_name, userInputName) + "\n" +
                getString(R.string.order_summary_pepperoni, boolToString(hasPepperoni)) + "\n" +
                getString(R.string.order_summary_italian_sausage, boolToString(hasItalianSausage)) + "\n" +
                getString(R.string.order_summary_mushrooms, boolToString(hasMushrooms)) + "\n" +
                getString(R.string.order_summary_black_olives, boolToString(hasBlackOlives)) + "\n" +
                getString(R.string.order_summary_green_peppers, boolToString(hasGreenPeppers)) + "\n" +
                getString(R.string.order_summary_pineapples, boolToString(hasPineapples)) + "\n" +
                getString(R.string.order_summary_quantity, quantity) + "\n" +
                getString(R.string.order_summary_total_price, price) + "\n" +
                getString(R.string.thank_you);
        return orderSummaryMessage;
    }

    /**
     * Method to calculate the total price
     *
     * @return total Price
     */
    private float  calculatePrice( boolean hasPepperoni, boolean hasBlackOlives, boolean hasGreenPeppers,
                                   boolean hasItalianSausage, boolean hasMushrooms, boolean hasPineapples){
        int basePrice = PIZZA_PRICE;
        if (hasPepperoni) {
            basePrice += PEPPERONI_PRICE;
        }
        if (hasBlackOlives) {
            basePrice += BLACK_OLIVES_PRICE;
        }
        if (hasGreenPeppers) {
            basePrice += GREEN_PEPPERS_PRICE;
        }
        if (hasItalianSausage) {
            basePrice += ITALIAN_SAUSAGE_PRICE;
        }
        if (hasMushrooms) {
            basePrice += MUSHROOM_PRICE;
        }
        if (hasPineapples) {
            basePrice += PINEAPPLES_PRICE;
        }
        return quantity * basePrice;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method increments the quantity of pizzas by one
     *
     * @param view on passes the view that we are working with to the method
     */

    public void increment(View view) {
        if (quantity < 100) {
            quantity = quantity + 1;
            display(quantity);
        } else {
            Log.i("MainActivity", "Please select less than one hundred pizzas.");
            Context context = getApplicationContext();
            String lowerLimitToast = getString(R.string.too_many_pizzas);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, lowerLimitToast, duration);
            toast.show();
            return;
        }
    }

    /**
     * This method decrements the quantity of pizzas by one
     *
     * @param view passes on the view that we are working with to the method
     */
    public void decrement(View view) {
        if (quantity > 1) {
            quantity = quantity - 1;
            display(quantity);
        } else {
            Log.i("MainActivity", "Please select at least one pizza");
            Context context = getApplicationContext();
            String upperLimitToast = getString(R.string.too_few_pizzas);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, upperLimitToast, duration);
            toast.show();
            return;
        }
    }
}