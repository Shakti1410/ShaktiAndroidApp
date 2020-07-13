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
import com.shakti.kisanmarket.Admin.AdminMaintainProductsActivity;
import com.shakti.kisanmarket.Admin.AdminUpdateProductActivity;
import com.shakti.kisanmarket.Model.Products;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.shakti.kisanmarket.ProductsReviewActivity;
import com.shakti.kisanmarket.R;
import com.shakti.kisanmarket.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class SellerUpdateProductsActivity extends AppCompatActivity {
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_update_products);
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.sellerrecycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


//        prod_del_btn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productRef.orderByChild("sellerPhone").equalTo(Prevalent.currentOnlineUser.getPhone().toString()),Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {

                        productViewHolder.review.setVisibility(View.INVISIBLE);
                        productViewHolder.txtproductName.setText(products.getPname());
                        productViewHolder.txtproductDescription.setText(products.getDescription());
                        productViewHolder.txtquantity.setText("Quantity = " + products.getQuantity() +" Kg");
                        productViewHolder.txtproductprice.setText("Price = " + products.getPrice() + "Rs");
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);


                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(SellerUpdateProductsActivity.this, AdminMaintainProductsActivity.class);
                                intent.putExtra("pid",products.getPid());
                                intent.putExtra("type","Seller");
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
        adapter.startListening();
    }
}
