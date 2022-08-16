package com.example.ecommerceapp.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapters.MyCartAdapter;
import com.example.ecommerceapp.models.MyCartModel;
import com.example.ecommerceapp.models.NewProductsModel;
import com.example.ecommerceapp.models.PopularProductsModel;
import com.example.ecommerceapp.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    int overAllTotalAmount;
    TextView overAllAmount;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<MyCartModel> cartModelList;
    Button buyNow;
    MyCartAdapter cartAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    NewProductsModel newProductsModel= null;
    PopularProductsModel popularProductsModel=null;
    ShowAllModel showAllModel=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        toolbar=findViewById(R.id.my_cart_toolbar);
     setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                                     finish();
                                                 }
        });



        overAllAmount=findViewById(R.id.textView4);
        recyclerView=findViewById(R.id.cart_rec);
        buyNow= findViewById(R.id.buy_now);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelList=new ArrayList<>();
        cartAdapter=new MyCartAdapter(this,cartModelList);
        recyclerView.setAdapter(cartAdapter);
        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()){
                for (DocumentSnapshot doc:task.getResult().getDocuments()){
                    String documentId= doc.getId();
                    MyCartModel myCartModel=doc.toObject(MyCartModel.class);
                    myCartModel.setDocumentId(documentId);
                    cartModelList.add(myCartModel);
                    cartAdapter.notifyDataSetChanged();
                }
                calculateTotalAmount(cartModelList);
            }
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(CartActivity.this,PlaceOrderActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelList);
                startActivity(intent);
            }
        });
    }

    private void calculateTotalAmount(List<MyCartModel> cartModelList) {
        double totalAmount= 0.0;
        for (MyCartModel myCartModel: cartModelList){
            totalAmount += myCartModel.getTotalPrice();
        }
        overAllAmount.setText("Total Amount :"+totalAmount);
    }

    public BroadcastReceiver mMessageReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalBill= intent.getIntExtra("totalAmount",0);
            overAllAmount.setText("Total Amount:"+totalBill+"$");

        }
    };

}