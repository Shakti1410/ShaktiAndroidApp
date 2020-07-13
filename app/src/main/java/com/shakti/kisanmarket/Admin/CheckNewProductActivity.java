package com.shakti.kisanmarket.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.shakti.kisanmarket.Inteface.ItemClickListner;
import com.shakti.kisanmarket.Model.AdminOrders;
import com.shakti.kisanmarket.Model.Products;
import com.shakti.kisanmarket.R;
import com.shakti.kisanmarket.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CheckNewProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverpRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_new_product);

        recyclerView = findViewById(R.id.admin_check_prod);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        unverpRef = FirebaseDatabase.getInstance().getReference().child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unverpRef.orderByChild("productState").equalTo("Not Approved"),Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {

                        productViewHolder.review.setVisibility(View.INVISIBLE);
                       productViewHolder.sellername.setVisibility(View.VISIBLE);
                       productViewHolder.sellerphone.setVisibility(View.VISIBLE);
                       productViewHolder.sellername.setText("Seller: " + products.getSellerName());
                       productViewHolder.sellerphone.setText("Phone: " + products.getSellerPhone());
                        productViewHolder.txtproductName.setText(products.getPname());
                        productViewHolder.txtquantity.setText("Quantity = " + products.getQuantity() +" Kg");
                        productViewHolder.txtproductDescription.setText(products.getDescription());
                        productViewHolder.txtproductprice.setText("Price = " + products.getPrice() + "Rs");
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                        final Products itemClick = products;
                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String productId = itemClick.getPid();
                                final String category = itemClick.getCategory();
                                final String pname = itemClick.getPname();

                                CharSequence option[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder =new AlertDialog.Builder(CheckNewProductActivity.this);
                                builder.setTitle("Do you want to approved this Product. Are you sure?");
                                builder.setItems(option, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if(i == 0)
                                        {
                                            changeProductState(productId,category,pname);
                                        }
                                        if(i== 1)
                                        {

                                        }
                                    }
                                });
                                builder.show();
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

    private void changeProductState(String productId, String category, String pname)
    {
        HashMap<String, Object> productmap = new HashMap<>();
        productmap.put("productState","Approved");
        productmap.put("categoryState",category+"Approved");
        productmap.put("nameState","Approved"+pname);
        unverpRef.child(productId).updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(CheckNewProductActivity.this, "Product has been Approved, and available for sale.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}

