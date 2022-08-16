package com.example.ecommerceapp.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PlaceOrderActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        firestore= FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        List<MyCartModel> list= (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList");

        if (list !=null && list.size() >0){
            for (MyCartModel model : list){
                final HashMap<String,Object> cartMap= new HashMap<>();

                cartMap.put("productName",model.getProductName());
                cartMap.put("productPrice",model.getProductPrice());
                cartMap.put("currentTime",model.getCurrentTime());
                cartMap.put("currentDate",model.getCurrentDate());
                cartMap.put("totalQuantity",model.getTotalQuantity());
                cartMap.put("totalPrice",model.getTotalPrice());

                firestore.collection("AddToCart").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                        .collection("MyOrder").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(PlaceOrderActivity.this, "Your Order Has Been Placed ",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }
    }
}