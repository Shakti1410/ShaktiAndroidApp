package com.shakti.kisanmarket.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shakti.kisanmarket.HomeActivity;
import com.shakti.kisanmarket.Model.Products;
import com.shakti.kisanmarket.ProductDetailsActivity;
import com.shakti.kisanmarket.ProductsReviewActivity;
import com.shakti.kisanmarket.R;
import com.shakti.kisanmarket.Sellers.SellerUpdateProductsActivity;
import com.shakti.kisanmarket.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
      //  homeViewModel =
     //           ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = root.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
       // final TextView textView = root.findViewById(R.id.text_home);

      /*  homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productRef.orderByChild("productState").equalTo("Approved"),Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                        productViewHolder.txtproductName.setText(products.getPname());
                        productViewHolder.txtproductDescription.setText(products.getDescription());
                        productViewHolder.txtproductprice.setText("Price = " + products.getPrice() + "Rs");
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {



                                Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                                intent.putExtra("pid",products.getPid());
                                intent.putExtra("availQuan",products.getQuantity());
                                intent.putExtra("psellername",products.getSellerName());
                                intent.putExtra("psellerphone",products.getSellerPhone());
                                startActivity(intent);


                            }
                        });
                        productViewHolder.review.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), ProductsReviewActivity.class);
                                intent.putExtra("pid",products.getPid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        //  adapter.notifyDataSetChanged();
        adapter.startListening();
    }
}