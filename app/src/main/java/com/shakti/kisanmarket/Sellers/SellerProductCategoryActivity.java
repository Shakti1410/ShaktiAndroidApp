package com.shakti.kisanmarket.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.shakti.kisanmarket.R;
import com.shakti.kisanmarket.SettingsActivity;
import com.shakti.kisanmarket.UserProductCategoryActivity;

public class SellerProductCategoryActivity extends AppCompatActivity {
    private ImageView cereals , pulses,vegetable,fruits,others;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_category);

        cereals = (ImageView)findViewById(R.id.cereals);
        pulses = (ImageView)findViewById(R.id.pulses);
        vegetable = (ImageView)findViewById(R.id.vegetable);
        fruits = (ImageView)findViewById(R.id.fruits);
        others = (ImageView)findViewById(R.id.others);


        cereals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                    intent.putExtra("category", "cereals");
                    startActivity(intent);


            }
        });
        pulses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "pulses");
                startActivity(intent);
            }
        });
        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "vegetables");
                startActivity(intent);
            }
        });
        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "fruits");
                startActivity(intent);
            }
        });
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "others");
                startActivity(intent);
            }
        });

    }
}
