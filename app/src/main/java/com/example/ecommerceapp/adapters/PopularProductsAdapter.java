package com.example.ecommerceapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.activites.DetailedActivity;
import com.example.ecommerceapp.models.PopularProductsModel;

import java.util.List;


public class PopularProductsAdapter extends RecyclerView.Adapter<PopularProductsAdapter.ViewHolder> {

    private Context context;
    private List<PopularProductsModel> popularProductsModelsList;

    public PopularProductsAdapter(Context context, List<PopularProductsModel> popularProductsModelsList) {
        this.context = context;
        this.popularProductsModelsList = popularProductsModelsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(popularProductsModelsList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(popularProductsModelsList.get(position).getName());
        holder.price.setText(String.valueOf(popularProductsModelsList.get(position).getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, DetailedActivity.class);
                intent.putExtra("detailed",popularProductsModelsList.get(position));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return popularProductsModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name,price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.all_img);
            name= itemView.findViewById(R.id.all_product_name);
            price= itemView.findViewById(R.id.all_price);
        }
    }
}
