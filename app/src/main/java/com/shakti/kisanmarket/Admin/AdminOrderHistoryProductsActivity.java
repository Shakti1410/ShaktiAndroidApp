package com.shakti.kisanmarket.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shakti.kisanmarket.Model.Cart;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.shakti.kisanmarket.R;
import com.shakti.kisanmarket.ViewHolder.CartViewHolder;

public class AdminOrderHistoryProductsActivity extends AppCompatActivity {
    private RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String key= "", id= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_history_products);


        key = getIntent().getStringExtra("key");
        id = getIntent().getStringExtra("id");
        productList = findViewById(R.id.user_his__adm_product_list);
        productList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);
        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart History")
                .child(id).child("Products").child(key);
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
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull Cart cart) {
                        holder.txtproductsellername.setVisibility(View.VISIBLE);
                        holder.product_feedback.setVisibility(View.GONE);
                        holder.txtProductquantity.setText("Quantity: "+cart.getQuantity() + " Kg");
                        holder.txtProductName.setText("Product: "+cart.getPname());
                        holder.txtproductsellername.setText("Seller: "+cart.getPsellername());
                        holder.txtProductprice.setText("Price: "+cart.getPrice() +" / Kg");

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
