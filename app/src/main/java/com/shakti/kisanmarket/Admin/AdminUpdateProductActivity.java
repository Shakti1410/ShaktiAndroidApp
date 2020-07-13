package com.shakti.kisanmarket.Admin;

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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shakti.kisanmarket.Model.Products;
import com.shakti.kisanmarket.ProductsReviewActivity;
import com.shakti.kisanmarket.R;
import com.shakti.kisanmarket.Sellers.SellerUpdateProductsActivity;
import com.shakti.kisanmarket.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class AdminUpdateProductActivity extends AppCompatActivity {

    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_product);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.adminrecycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productRef.orderByChild("productState").equalTo("Approved"),Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {

                        productViewHolder.sellername.setVisibility(View.VISIBLE);
                        productViewHolder.sellerphone.setVisibility(View.VISIBLE);
                        productViewHolder.sellername.setText("Seller: " + products.getSellerName());
                        productViewHolder.sellerphone.setText("Phone: " + products.getSellerPhone());
                        productViewHolder.txtproductName.setText(products.getPname());
                        productViewHolder.txtquantity.setText("Quantity = " + products.getQuantity() +" Kg");
                        productViewHolder.txtproductDescription.setText(products.getDescription());
                        productViewHolder.txtproductprice.setText("Price = " + products.getPrice() + "Rs");
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);


                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                    Intent intent = new Intent(AdminUpdateProductActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid",products.getPid());
                                    intent.putExtra("type","Admin");
                                    startActivity(intent);


                            }
                        });
                        productViewHolder.review.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AdminUpdateProductActivity.this, ProductsReviewActivity.class);
                                intent.putExtra("pid",products.getPid());
                                intent.putExtra("type","Admin");
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
