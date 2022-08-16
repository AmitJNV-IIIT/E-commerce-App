package com.example.ecommerceapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.ULocale;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.activites.ShowAllActivity;
import com.example.ecommerceapp.adapters.CategoryAdapter;
import com.example.ecommerceapp.adapters.NewProductsAdapter;
import com.example.ecommerceapp.adapters.PopularProductsAdapter;
import com.example.ecommerceapp.adapters.ShowAllAdapter;
import com.example.ecommerceapp.models.CategoryModel;
import com.example.ecommerceapp.models.NewProductsModel;
import com.example.ecommerceapp.models.PopularProductsModel;
import com.example.ecommerceapp.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    TextView catShowAll,popularShowAll,newProductShowAll;
    LinearLayout linearLayout;
    ProgressDialog progressDialog;

    RecyclerView catRecycleView,newProductRecyclerview,popularRecyclerview;

    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    NewProductsAdapter newProductsAdapter;
    List<NewProductsModel> newProductsModelList;

    PopularProductsAdapter popularProductsAdapter;
    List<PopularProductsModel> popularProductsModelList;

    EditText search_box;
    private List<ShowAllModel> showAllModelList;
    private RecyclerView recyclerViewSearch;
    private ShowAllAdapter showAllAdapter;

    FirebaseFirestore db ;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View root= inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();

      progressDialog= new ProgressDialog(getActivity());
     catRecycleView = root.findViewById(R.id.rec_category);
     newProductRecyclerview= root.findViewById(R.id.new_product_rec);
     popularRecyclerview= root.findViewById(R.id.popular_rec);

     catShowAll= root.findViewById(R.id.category_see_all);
        popularShowAll= root.findViewById(R.id.popular_see_all);
        newProductShowAll= root.findViewById(R.id.newProducts_see_all);

        catShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        newProductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });
       popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });


        linearLayout= root.findViewById(R.id.home_layout);
        linearLayout.setVisibility(View.GONE);

        ImageSlider imageSlider= root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels= new ArrayList<>();





        slideModels.add(new SlideModel(R.drawable.banner1,"Discount On Shoes Items", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2,"Discount On Perfume", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3,"70% OFF", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);

        progressDialog.setTitle("Welcome To My ECommerce App");
        progressDialog.setMessage("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

      catRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        categoryModelList= new ArrayList<>();
        categoryAdapter= new CategoryAdapter(getContext(),categoryModelList);
       catRecycleView.setAdapter(categoryAdapter);

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoryModel categoryModel= document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                                linearLayout.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getActivity(),""+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        newProductRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        newProductsModelList= new ArrayList<>();
        newProductsAdapter= new NewProductsAdapter(getContext(),newProductsModelList);
        newProductRecyclerview.setAdapter(newProductsAdapter);

        db.collection("NewProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                              NewProductsModel newProductsModel= document.toObject(NewProductsModel.class);
                                newProductsModelList.add(newProductsModel);
                                newProductsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(),""+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        popularRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));
       popularProductsModelList= new ArrayList<>();
       popularProductsAdapter= new PopularProductsAdapter(getContext(),popularProductsModelList);
       popularRecyclerview.setAdapter(popularProductsAdapter);

        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PopularProductsModel popularProductsModel= document.toObject(PopularProductsModel.class);
                                popularProductsModelList.add(popularProductsModel);
                               popularProductsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(),"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

       recyclerViewSearch= root.findViewById(R.id.search_rec);
       search_box= root.findViewById(R.id.search_box);
       showAllModelList= new ArrayList<>();
       showAllAdapter= new ShowAllAdapter(getContext(),showAllModelList);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(showAllAdapter);
        recyclerViewSearch.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().isEmpty()){
                    showAllModelList.clear();
                    showAllAdapter.notifyDataSetChanged();
                }else {
                    searchProduct(editable.toString());
                }
            }
        });


        return root;

    }

    private void searchProduct(String type) {
        if (!type.isEmpty()){
            db.collection("ShowAll").whereEqualTo("type",type).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                          if (task.isSuccessful() && task.getResult() !=null){
                              showAllModelList.clear();
                              showAllAdapter.notifyDataSetChanged();
                              for (DocumentSnapshot doc : task.getResult().getDocuments()){
                                  ShowAllModel showAllModel= doc.toObject(ShowAllModel.class);
                                  showAllModelList.add(showAllModel);
                                  showAllAdapter.notifyDataSetChanged();
                              }
                          }
                        }
                    });
        }
    }
}