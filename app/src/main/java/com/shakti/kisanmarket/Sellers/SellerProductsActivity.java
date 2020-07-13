package com.shakti.kisanmarket.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shakti.kisanmarket.MainActivity;
import com.shakti.kisanmarket.Model.Products;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.shakti.kisanmarket.ProductsReviewActivity;
import com.shakti.kisanmarket.R;
import com.shakti.kisanmarket.ViewHolder.ItemViewHolder;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class SellerProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverpRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products);


        recyclerView = findViewById(R.id.seller_home_recyV);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        unverpRef = FirebaseDatabase.getInstance().getReference().child("Products");



    }



    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unverpRef.orderByChild("sellerPhone").equalTo(Prevalent.currentOnlineUser.getPhone().toString()),Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder productViewHolder, int i, @NonNull final Products products) {
                        productViewHolder.review.setVisibility(View.VISIBLE);
                        productViewHolder.txtproductName.setText(products.getPname());
                        productViewHolder.txtproductDescription.setText(products.getDescription());
                        productViewHolder.txtquantity.setText("Quantity = " + products.getQuantity() +" Kg");
                        productViewHolder.txtproductState.setText("State = " +products.getProductState());
                        productViewHolder.txtproductprice.setText("Price = " + products.getPrice() + "Rs");
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                        productViewHolder.review.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SellerProductsActivity.this, ProductsReviewActivity.class);
                                intent.putExtra("pid",products.getPid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view,parent,false);
                        ItemViewHolder holder = new ItemViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
