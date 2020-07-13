package com.shakti.kisanmarket.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.shakti.kisanmarket.HomeActivity;
import com.shakti.kisanmarket.MainActivity;
import com.shakti.kisanmarket.R;

import io.paperdb.Paper;

public class AdminHomeActivity extends AppCompatActivity {

    private Button logout;
    private ImageView chck_aprv_btn,check_order_btn,update_product_btn,seller_det_btn,user_det_btn,order_his_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logout = (Button)findViewById(R.id.admin_logout);
        chck_aprv_btn = (ImageView) findViewById(R.id.approve_product_btn);
        update_product_btn = (ImageView)findViewById(R.id.update_detail_btn);
        check_order_btn = (ImageView)findViewById(R.id.chech_orders_btn);
        seller_det_btn = (ImageView)findViewById(R.id.seller_list_btn);
        user_det_btn = (ImageView)findViewById(R.id.buyer_list_btn);
        order_his_btn = (ImageView)findViewById(R.id.order_his_btn);


                logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(AdminHomeActivity.this,R.style.Theme_AppCompat_Light_Dialog)
                        .setTitle("Logout")
                        .setMessage("Are you sure,you want to Exit?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Paper.book().destroy();
                                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Cancel",null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        check_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });
        update_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminUpdateProductActivity.class);
               // intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });
        chck_aprv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, CheckNewProductActivity.class);
                startActivity(intent);
            }
        });

        seller_det_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, SellerDetailsActivity.class);
                startActivity(intent);
            }
        });
        user_det_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, UsersDetailsActivity.class);
                startActivity(intent);
            }
        });
        order_his_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminOderHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed() {
        new AlertDialog.Builder(AdminHomeActivity.this,
                R.style.Theme_AppCompat_Light_Dialog).setTitle("Logout")
                .setMessage("Are you sure? you want to exit?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    }
                }).setNegativeButton("Cancel",null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
