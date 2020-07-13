package com.shakti.kisanmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shakti.kisanmarket.Model.Products;
import com.shakti.kisanmarket.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class UserProCatViewActivity extends AppCompatActivity {
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String protype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pro_cat_view);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        protype = getIntent().getStringExtra("category");
        recyclerView = findViewById(R.id.recycler_menu_user);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productRef.orderByChild("categoryState").equalTo(protype+"Approved"),Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products)
                    {
                        productViewHolder.txtproductName.setText(products.getPname());
                        productViewHolder.txtproductDescription.setText(products.getDescription());
                        productViewHolder.txtproductprice.setText("Price = " + products.getPrice() + "Rs");
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(UserProCatViewActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pid", products.getPid());
                                intent.putExtra("psellername", products.getSellerName());
                                intent.putExtra("psellerphone", products.getSellerPhone());
                                startActivity(intent);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
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