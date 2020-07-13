package com.shakti.kisanmarket.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shakti.kisanmarket.R;
import com.shakti.kisanmarket.Sellers.SellerProductCategoryActivity;
import com.shakti.kisanmarket.Sellers.SellerUpdateProductsActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    private Button update_btn,del_btn;
    private EditText name,price,desription,quantity;
    private ImageView imageView;
    private String productId="",type ="";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId = getIntent().getStringExtra("pid");
        type = getIntent().getStringExtra("type");

        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
        update_btn =(Button)findViewById(R.id.update_product_btn);
        del_btn =(Button)findViewById(R.id.del_product_btn);
        name =(EditText) findViewById(R.id.update_product_name);
        quantity = (EditText)findViewById(R.id.update_product_quantity);
        price =(EditText) findViewById(R.id.update_product_price);
        desription =(EditText) findViewById(R.id.update_product_Descrip);
        imageView =(ImageView) findViewById(R.id.update_product_image);

        displayproductInfo();

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdminMaintainProductsActivity.this,R.style.Theme_AppCompat_Light_Dialog).setTitle("Delete Product")
                        .setMessage("Are you sure? you want to delete this Product?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // String scls = spinner.getSelectedItem().toString();

                                productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(AdminMaintainProductsActivity .this,"Product Deleted Successfully.", Toast.LENGTH_SHORT).show();
                                            if(type.equals("Admin"))
                                            {
                                                Intent intent = new Intent(AdminMaintainProductsActivity.this,AdminUpdateProductActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                 startActivity(intent);
                                                   finish();
                                            }
                                            else  if(type.equals("Seller"))
                                            {
                                                Intent intent = new Intent(AdminMaintainProductsActivity.this, SellerUpdateProductsActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                 startActivity(intent);
                                                   finish();
                                            }

                                        }
                                        else {
                                            Toast.makeText(AdminMaintainProductsActivity.this,"Failed to Delete.", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel",null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void applyChanges()
    {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDes = desription.getText().toString();
        String quanity = quantity.getText().toString();

        if(pName.equals(""))
        {
            Toast.makeText(this, "Enter Product Name.", Toast.LENGTH_SHORT).show();
        }
        else   if(pPrice.equals(""))
        {
            Toast.makeText(this, "Enter Product Price.", Toast.LENGTH_SHORT).show();
        }
        else   if(pDes.equals(""))
        {
            Toast.makeText(this, "Enter Product Description.", Toast.LENGTH_SHORT).show();
        }
        else   if(quanity.equals(""))
        {
            Toast.makeText(this, "Enter Product Quantity.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productmap = new HashMap<>();
            productmap.put("pid",productId);
            productmap.put("description",pDes);
            productmap.put("price",pPrice);
            productmap.put("pname",pName);
            productmap.put("quantity",quanity);

            productRef.updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

    }

    private void displayproductInfo() {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
             if(snapshot.exists())
             {
                 String proname = snapshot.child("pname").getValue().toString();
                 String proprice = snapshot.child("price").getValue().toString();
                 String prodecrip = snapshot.child("description").getValue().toString();
                 String quant = snapshot.child("quantity").getValue().toString();
                 String pimage = snapshot.child("image").getValue().toString();
                 name.setText(proname);
                 price.setText(proprice);
                 desription.setText(prodecrip);
                 quantity.setText(quant);
                 Picasso.get().load(pimage).into(imageView);

             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
