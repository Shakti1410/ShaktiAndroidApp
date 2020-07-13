package com.shakti.kisanmarket;

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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shakti.kisanmarket.Model.Cart;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.shakti.kisanmarket.ViewHolder.CartViewHolder;

import java.util.HashMap;

public class FinalOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button next_btn;
    private TextView totalPrice,txtmsg1;
    private double overTotalPrice = 0;
    private DatabaseReference carthistoryRef, orderhistRef,orderRef;
    private String key,pid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order);

        carthistoryRef = FirebaseDatabase.getInstance().getReference().child("Cart History");
        orderhistRef = FirebaseDatabase.getInstance().getReference().child("Orders History")
                .child(Prevalent.currentOnlineUser.getPhone());
         orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        recyclerView = findViewById(R.id.cart_list_f);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        key = getIntent().getStringExtra("key");
        pid = getIntent().getStringExtra("pid");
        next_btn = (Button)findViewById(R.id.next_btn_f);
        totalPrice = (TextView)findViewById(R.id.total_price_f);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Cart List")
                        .child("User View").child(Prevalent.currentOnlineUser.getPhone())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful())
                       {
                           Toast.makeText(FinalOrderActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();

                           Intent intent = new Intent(FinalOrderActivity.this, HomeActivity.class);
                         //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);
                           finish();
                       }
                    }
                });

                }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference cartListref = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListref.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                        .child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull Cart cart) {
                        holder.txtProductquantity.setText("Quantity = "+cart.getQuantity() + " Kg");
                        holder.txtProductName.setText(cart.getPname());
                        holder.txtProductprice.setText("Price = "+cart.getPrice()+ " / Kg");
                        Double oneTypeProductTprice = (Double.valueOf(cart.getPrice())) * (Integer.valueOf(cart.getQuantity()));
                        overTotalPrice = overTotalPrice + oneTypeProductTprice;
                        totalPrice.setText("Total Price = Rs. "+String.valueOf(overTotalPrice));

                        String pid = cart.getPid();
                        String name = cart.getPname();
                        String quan = cart.getQuantity();
                        String price = cart.getPrice();
                        String sname = cart.getPsellername();
                        String sellerphone = cart.getPsellerphone();

                        final HashMap<String,Object> cartMap = new HashMap<>();
                        cartMap.put("pid",pid);
                        cartMap.put("psellername",sname);
                        cartMap.put("psellerphone",sellerphone);
                        cartMap.put("pname",name);
                        cartMap.put("price",price);
                        cartMap.put("quantity",quan);

                        carthistoryRef.child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(key).child(pid)
                                .updateChildren(cartMap);


                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public void onBackPressed() {
        new AlertDialog.Builder(FinalOrderActivity.this,
                R.style.Theme_AppCompat_Light_Dialog).setTitle("Exit")
                .setMessage("Are you sure? you want to exit without placed order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        carthistoryRef.child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                .child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                              if (task.isSuccessful())
                              {
                                  orderhistRef.child(pid).removeValue();
                                  orderRef.removeValue();
                                  Intent intent = new Intent(FinalOrderActivity.this, HomeActivity.class);
                                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                  startActivity(intent);
                                  finish();
                              }
                            }
                        });



                    }
                }).setNegativeButton("Cancel",null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
