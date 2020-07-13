package com.shakti.kisanmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.shakti.kisanmarket.Sellers.SellerAddNewProductActivity;
import com.shakti.kisanmarket.Sellers.SellerProductCategoryActivity;

public class UserProductCategoryActivity extends AppCompatActivity {
    private ImageView cereals , pulses,vegetable,fruits,others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_category);

        cereals = (ImageView)findViewById(R.id.cereals_u);
        pulses = (ImageView)findViewById(R.id.pulses_u);
        vegetable = (ImageView)findViewById(R.id.vegetable_u);
        fruits = (ImageView)findViewById(R.id.fruits_u);
        others = (ImageView)findViewById(R.id.others_u);


        cereals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserProductCategoryActivity.this, UserProCatViewActivity.class);
                intent.putExtra("category", "cereals");
                startActivity(intent);


            }
        });
        pulses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProductCategoryActivity.this, UserProCatViewActivity.class);
                intent.putExtra("category", "pulses");
                startActivity(intent);
            }
        });
        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProductCategoryActivity.this, UserProCatViewActivity.class);
                intent.putExtra("category", "vegetables");
                startActivity(intent);
            }
        });
        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProductCategoryActivity.this, UserProCatViewActivity.class);
                intent.putExtra("category", "fruits");
                startActivity(intent);
            }
        });
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProductCategoryActivity.this, UserProCatViewActivity.class);
                intent.putExtra("category", "others");
                startActivity(intent);
            }
        });
    }
}
