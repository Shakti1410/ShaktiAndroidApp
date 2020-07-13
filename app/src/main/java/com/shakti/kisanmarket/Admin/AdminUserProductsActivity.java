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
import com.shakti.kisanmarket.R;
import com.shakti.kisanmarket.ViewHolder.CartViewHolder;

public class AdminUserProductsActivity extends AppCompatActivity {
    private RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String userId= "", pid ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userId = getIntent().getStringExtra("uid");
        pid = getIntent().getStringExtra("pid");
        productList = findViewById(R.id.admin_product_list);
        productList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);
        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
        .child("Admin View").child(userId).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options =
        new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef,Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull Cart cart) {
                holder.txtproductsellername.setVisibility(View.VISIBLE);
                holder.txtsellerphone.setVisibility(View.VISIBLE);
                holder.txtProductquantity.setText("Quantity: "+cart.getQuantity()+" Kg");
                holder.txtProductName.setText(cart.getPname());
                holder.txtproductsellername.setText("Seller: "+cart.getPsellername());
                holder.txtsellerphone.setText("Seller Phone: "+cart.getPsellerphone());
                holder.txtProductprice.setText("Price: "+cart.getPrice()+" /Kg");
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
