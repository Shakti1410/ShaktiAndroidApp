package com.shakti.kisanmarket.Sellers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.shakti.kisanmarket.Admin.AdminUpdateProductActivity;
import com.shakti.kisanmarket.MainActivity;
import com.shakti.kisanmarket.R;

import io.paperdb.Paper;

public class SellerHomeActivity extends AppCompatActivity {
    private Button logout;
    private ImageView allprod_btn,addprod_btn,update_prod_btn,order_his_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        logout = (Button)findViewById(R.id.seller_logout);
        allprod_btn = (ImageView) findViewById(R.id.check_product_btn);
        addprod_btn = (ImageView)findViewById(R.id.add_product_btn);
        update_prod_btn = (ImageView)findViewById(R.id.update_sel_prod_btn);
        order_his_btn = (ImageView)findViewById(R.id.sel_order_his);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutHome();
            }
        });
        addprod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                startActivity(intent);
            }
        });
        allprod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerHomeActivity.this, SellerProductsActivity.class);
                startActivity(intent);
            }
        });
        update_prod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerHomeActivity.this, SellerUpdateProductsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void logoutHome()
    {
        new AlertDialog.Builder(SellerHomeActivity.this,R.style.Theme_AppCompat_Light_Dialog)
                .setTitle("Logout")
                .setMessage("Are you sure,you want to Exit?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Paper.book().destroy();
                        Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("Cancel",null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void onBackPressed() {
        new AlertDialog.Builder(SellerHomeActivity.this,
                R.style.Theme_AppCompat_Light_Dialog).setTitle("Logout")
                .setMessage("Are you sure? you want to exit?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    }
                }).setNegativeButton("Cancel",null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
