package com.shakti.kisanmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shakti.kisanmarket.Model.Feedback;
import com.shakti.kisanmarket.Model.Products;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProductFedbackActivity extends AppCompatActivity {
    private EditText rate,review;
    private Button feed_btn;
    DatabaseReference ref;
    String pname="",pid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_fedback);

        pid = getIntent().getStringExtra("pid");
        pname = getIntent().getStringExtra("pname");
        rate = (EditText)findViewById(R.id.rating_txt);
        review = (EditText)findViewById(R.id.review_txt);
        feed_btn = (Button)findViewById(R.id.add_feedback_btn);
        ref = FirebaseDatabase.getInstance().getReference().child("Review").child(pid).child(Prevalent.currentOnlineUser.getPhone());
        getProductDetails(pid);
        feed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatereview();
            }
        });

    }

    private void getProductDetails(String pid)
    {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    Feedback feedback = snapshot.getValue(Feedback.class);

                 rate.setText(feedback.getRating());
                 review.setText(feedback.getReview());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updatereview()
    {
        String name = Prevalent.currentOnlineUser.getName();
        String rating = rate.getText().toString();
        String rev = review.getText().toString();
        if(TextUtils.isEmpty(rating))
        {
            Toast.makeText(this, "Please Enter Rating ", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(rev))
        {
            Toast.makeText(this, "Please give your valuable Review ", Toast.LENGTH_SHORT).show();
        }
        else if(Integer.parseInt(rating) <=0)
        {
            Toast.makeText(this, "Give Rating between 1 - 5 ", Toast.LENGTH_SHORT).show();

        }
        else if( Integer.parseInt(rating)>5 )
        {
            Toast.makeText(this, "Give Rating between 1 - 5 ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productmap = new HashMap<>();
            productmap.put("pid",pid);
            productmap.put("pname",pname);
            productmap.put("uname",name);
            productmap.put("rating",rating);
            productmap.put("review",rev);
            productmap.put("uphone",Prevalent.currentOnlineUser.getPhone());
            ref.updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ProductFedbackActivity.this, "Thank you for your valuable feedback", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProductFedbackActivity.this, HomeActivity.class);
                     //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                      //  finish();
                    }

                }
            });
        }
    }

}
