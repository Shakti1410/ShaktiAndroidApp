package com.shakti.kisanmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shakti.kisanmarket.Model.Users;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.shakti.kisanmarket.Sellers.SellerRegistrationActivity;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNow_btn,login_btn;
    private ProgressDialog loadingbar;
    private TextView seller_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNow_btn = findViewById(R.id.main_join_now_btn);
        login_btn = findViewById(R.id.main_login_btn);
        seller_tv = findViewById(R.id.seller_tv);
        loadingbar = new ProgressDialog(this);

        Paper.init(this);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        joinNow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        seller_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });
        String userPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String userPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if(userPhoneKey != "" && userPasswordKey != "")
        {
            if(!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey))
            {
                loadingbar.setTitle("Already Logged In");
                loadingbar.setMessage("Please wait.....");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                AllowAcces(userPhoneKey,userPasswordKey);
            }
        }
    }

    private void AllowAcces(final String phone, final String paswd) {
        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(paswd))
                        {
                            Toast.makeText(MainActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            Prevalent.currentOnlineUser=usersData;
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }

                }
                else {
                    Toast.makeText(MainActivity.this, "Account Not Exist." , Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
