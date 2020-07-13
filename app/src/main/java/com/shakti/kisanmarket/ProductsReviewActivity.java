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
import com.shakti.kisanmarket.Admin.AdminMaintainProductsActivity;
import com.shakti.kisanmarket.Admin.AdminUpdateProductActivity;
import com.shakti.kisanmarket.Model.Cart;
import com.shakti.kisanmarket.Model.Feedback;
import com.shakti.kisanmarket.Sellers.SellerUpdateProductsActivity;
import com.shakti.kisanmarket.ViewHolder.CartViewHolder;

public class ProductsReviewActivity extends AppCompatActivity {
    private RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String pid ="",type ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_review);

        type = getIntent().getStringExtra("type");
        pid = getIntent().getStringExtra("pid");
        productList = findViewById(R.id.feedback_list);
        productList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);
        cartListRef = FirebaseDatabase.getInstance().getReference().child("Review").child(pid);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Feedback> options =
                new FirebaseRecyclerOptions.Builder<Feedback>()
                        .setQuery(cartListRef.orderByChild("pid").equalTo(pid),Feedback.class)
                        .build();
        FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder> adapter =
                new FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FeedbackViewHolder holder, int i, @NonNull final Feedback model) {

                        if(type != null)
                        {
                            holder.feedback_btn.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            holder.feedback_btn.setVisibility(View.GONE);
                        }

                        holder.userName.setText("User Name :" + model.getUname());
                        holder.pname.setText("Product Name :" + model.getPname());
                        holder.rating.setText("Rating :" + model.getRating() + " / 5");
                        holder.review.setText("Review :" + model.getReview() );
                        holder.feedback_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                new AlertDialog.Builder(ProductsReviewActivity.this,R.style.Theme_AppCompat_Light_Dialog).setTitle("Delete Feedback")
                                        .setMessage("Are you sure? you want to delete this Product?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // String scls = spinner.getSelectedItem().toString();
                                                removeFeedback(model.getUphone());

                                            }
                                        }).setNegativeButton("Cancel",null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedbacklist_layout,parent,false);
                        return new FeedbackViewHolder(view);
                    }
                };
        productList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeFeedback(String uphone) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Review").child(pid).child(uphone);
        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(ProductsReviewActivity.this, "Feedback Remove Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProductsReviewActivity.this, AdminUpdateProductActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName,pname,rating,review;
        public Button feedback_btn;
        public FeedbackViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.feedback_user_name);
            pname = itemView.findViewById(R.id.feedback_product_name);
            rating = itemView.findViewById(R.id.feedback_product_rating);
            review = itemView.findViewById(R.id.user_feedback);
            feedback_btn = itemView.findViewById(R.id.feedback_del);

        }
    }
}
