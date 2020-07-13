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
import com.shakti.kisanmarket.Admin.AdminNewOrdersActivity;
import com.shakti.kisanmarket.Admin.AdminUserProductsActivity;
import com.shakti.kisanmarket.Model.Cart;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.shakti.kisanmarket.ViewHolder.CartViewHolder;

public class UserOrderHistoryProductsActivity extends AppCompatActivity {
    private RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String key= "", pid ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_history_products);

       // userId = getIntent().getStringExtra("uid");
        pid = getIntent().getStringExtra("pid");
        key = getIntent().getStringExtra("key");
        productList = findViewById(R.id.user_his_product_list);
        productList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);
        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart History")
               .child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(key);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options =
            new FirebaseRecyclerOptions.Builder<Cart>()
                    .setQuery(cartListRef,Cart.class)
                    .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull final Cart cart) {
                        holder.txtproductsellername.setVisibility(View.VISIBLE);
                        holder.product_feedback.setVisibility(View.VISIBLE);
                        holder.txtProductquantity.setText("Quantity: "+cart.getQuantity() + " Kg");
                        holder.txtProductName.setText("Product: "+cart.getPname());
                        holder.txtproductsellername.setText("Seller: "+cart.getPsellername());
                        holder.txtProductprice.setText("Price: "+cart.getPrice() +" / Kg");


                        holder.product_feedback.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(UserOrderHistoryProductsActivity.this, ProductFedbackActivity.class);
                                intent.putExtra("pid",cart.getPid());
                                intent.putExtra("pname",cart.getPname());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };
        productList.setAdapter(adapter);
        adapter.startListening();

    }
}
