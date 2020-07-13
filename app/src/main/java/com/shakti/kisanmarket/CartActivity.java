package com.shakti.kisanmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shakti.kisanmarket.Model.Cart;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.shakti.kisanmarket.ViewHolder.CartViewHolder;

import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button next_btn;
    private TextView totalPrice,txtmsg1;
    private double overTotalPrice = 0;
    private DatabaseReference carthistoryRef;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        next_btn = (Button)findViewById(R.id.next_btn);
        totalPrice = (TextView)findViewById(R.id.total_price);
        txtmsg1 = (TextView)findViewById(R.id.msg1);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( overTotalPrice< 3000)
                {
                    Toast.makeText(CartActivity.this, "Order Value must have 3000 and above. ", Toast.LENGTH_SHORT).show();

                }
                else {

                    Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                    intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderstate();

           String uid = Prevalent.currentOnlineUser.getPhone();
        final DatabaseReference cartListref = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListref.child("User View").child(uid)
                        .child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull final Cart cart)
            {

                holder.txtProductquantity.setText("Quantity = "+cart.getQuantity()+ " Kg");
                holder.txtProductName.setText(cart.getPname());
                holder.txtProductprice.setText("Price = "+cart.getPrice() + " / Kg");
                Double oneTypeProductTprice = (Double.valueOf(cart.getPrice())) * (Integer.valueOf(cart.getQuantity()));
                overTotalPrice = overTotalPrice + oneTypeProductTprice;
                totalPrice.setText("Total Price = Rs. "+String.valueOf(overTotalPrice));


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]= new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                             if(i==0)
                             {
                                 cartListref.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                         .child("Products").child(cart.getPid())
                                         .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         cartListref.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                                 .child("Products").child(cart.getPid())
                                                 .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if(task.isSuccessful())
                                                 {
                                                     Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                                     intent.putExtra("pid",cart.getPid());
                                                     startActivity(intent);
                                                 }
                                             }
                                         });

                                     }
                                 });

                             }
                             if(i==1)
                             {
                                 cartListref.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                         .child("Products").child(cart.getPid())
                                         .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         cartListref.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                                 .child("Products").child(cart.getPid())
                                                 .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if(task.isSuccessful())
                                                 {
                                                     Toast.makeText(CartActivity.this, "Item Removed Successfully. ", Toast.LENGTH_SHORT).show();
                                                     Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                                     startActivity(intent);
                                                 }
                                             }
                                         });

                                     }
                                 });
                             }

                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void CheckOrderstate()
    {

        DatabaseReference orderRef;

            orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
            orderRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        String shippingState = snapshot.child("state").getValue().toString();
                        String userName = snapshot.child("name").getValue().toString();

                        if (shippingState.equals("Shipped")) {
                            totalPrice.setText("Dear " + userName + "\n order shipped successfully.");
                            recyclerView.setVisibility(View.GONE);
                            txtmsg1.setVisibility(View.VISIBLE);
                            txtmsg1.setText("Congratulation,Your Order has been Shipped Successfully. Soon you will received.");
                            next_btn.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "You can purchase more products, once you received your  previous order.", Toast.LENGTH_LONG).show();
                        } else if (shippingState.equals("not Shipped")) {
                            totalPrice.setText("Shipping State = Not Shipped");
                            recyclerView.setVisibility(View.GONE);
                            txtmsg1.setVisibility(View.VISIBLE);
                            next_btn.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "You can purchase more products, once you received your  previous order.", Toast.LENGTH_LONG).show();


                        }
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }
}
