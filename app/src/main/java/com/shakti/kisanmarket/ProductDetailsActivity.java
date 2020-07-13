package com.shakti.kisanmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shakti.kisanmarket.Model.Products;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

public class ProductDetailsActivity extends AppCompatActivity {
  //  private FloatingActionButton addTocart;
    private ImageView productImage;
    private EditText number_btn;
    private Button add_to_cart_btn;
    private TextView productPrice,productDescription,productName;
    private String productId="",state = "Normal",sellername="",sellerphone="",availableQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("pid");
        sellername = getIntent().getStringExtra("psellername");
        sellerphone = getIntent().getStringExtra("psellerphone");

        add_to_cart_btn = (Button)findViewById(R.id.pd_add_to_cart_btn);
       // addTocart = (FloatingActionButton)findViewById(R.id.add_product_cart);
        number_btn =(EditText)findViewById(R.id.number_btn);
        productImage =(ImageView)findViewById(R.id.product_image_details);
        productPrice =(TextView)findViewById(R.id.details_productPrice);
        productDescription =(TextView)findViewById(R.id.details_product_description);
        productName =(TextView)findViewById(R.id.product_name_details);

        getProductDetails(productId);

        add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state.equals("Order Placed") ||state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "You can purchase more products, once you received your  previous order.", Toast.LENGTH_LONG).show();

                }
                else
                {

                    addingTocartList();
                }
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();
        CheckOrderstate();
    }

    private void addingTocartList()
    {

        final String saveCurrentTime,saveCurrentDate,key;
        Calendar calFordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calFordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calFordate.getTime());

        String quantity = number_btn.getText().toString();
        if(Integer.parseInt(quantity) <= Integer.parseInt(availableQ)) {
            final DatabaseReference cartlistRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("pid", productId);
            cartMap.put("psellername", sellername);
            cartMap.put("psellerphone", sellerphone);
            cartMap.put("pname", productName.getText().toString());
            cartMap.put("price", productPrice.getText().toString());
            cartMap.put("date", saveCurrentDate);
            cartMap.put("time", saveCurrentTime);
            cartMap.put("quantity", quantity);
            cartMap.put("discount", "");

            cartlistRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                    .child("Products").child(productId)
                    .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        cartlistRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                .child("Products").child(productId)
                                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()) {
                                    Toast.makeText(ProductDetailsActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                    startActivity(intent);

                                }


                            }
                        });
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Product Quantity not available. please enter available quantity", Toast.LENGTH_SHORT).show();
        }


    }

    private void getProductDetails(String productId)
    {
        DatabaseReference producRef = FirebaseDatabase.getInstance().getReference().child("Products");

        producRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    Products products = snapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    number_btn.setText(products.getQuantity());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                    availableQ = products.getQuantity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void CheckOrderstate()
    {

        DatabaseReference orderRef;
        orderRef  = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {

                    String shippingState = snapshot.child("state").getValue().toString();

                    if(shippingState.equals("Shipped"))
                    {
                      state = "Order Shipped";
                    }
                    else if(shippingState.equals("not Shipped"))
                    {
                        state = "Order Placed";

                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
