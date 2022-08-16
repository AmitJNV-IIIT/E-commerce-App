package com.example.ecommerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.activites.CartActivity;
import com.example.ecommerceapp.activites.PlaceOrderActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.CollationElementIterator;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    double amount = 0.0;
    double totalamount=0.0;
    Toolbar toolbar;
    TextView subTotal,discount,shipping,total;
    Button paymentBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        //Toolbar
        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        amount = getIntent().getDoubleExtra("amount",0.0);
//cart
        totalamount= getIntent().getDoubleExtra("Totalamount",0.0);


        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);
        paymentBtn = findViewById(R.id.pay_btn);

        subTotal.setText(amount+"$");
        total.setText(amount+"$");


        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod();
            }
        });


    }

    private void paymentMethod() {

        Checkout checkout = new Checkout();

        final Activity activity = PaymentActivity.this;

        try {
            JSONObject options = new JSONObject();
            //Set Company Name
            options.put("name", "My Amit's E-Commerce App");
            //Ref no
            options.put("description", "Reference No. #123456");
            //Image to be display
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            // Currency type
            options.put("currency", "USD");
            //double total = Double.parseDouble(mAmountText.getText().toString());
            //multiply with 100 to get exact amount in rupee
            amount = amount* 100 ;
            //amount
            options.put("amount", amount);
            options.put("Totalamount", totalamount);
            JSONObject preFill = new JSONObject();
            //email
            preFill.put("email", " amit@iiitmanipur.ac.in");
            //contact
            preFill.put("contact", " 7706938054");

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(PaymentActivity.this, PlaceOrderActivity.class);

        startActivity(intent);
    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, "Payment Cancel", Toast.LENGTH_SHORT).show();
    }
}
