package com.shakti.kisanmarket.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shakti.kisanmarket.Model.Users;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.shakti.kisanmarket.R;

public class SellerLoginActivity extends AppCompatActivity {

    private EditText phoneNumber,password;
    private Button login_btn;
    private ProgressDialog loadingbar;
    private String parentDbName = "Seller";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        login_btn = (Button)findViewById(R.id.seller_login_btn);
        phoneNumber = (EditText) findViewById(R.id.seller_login_ph);
        password = (EditText) findViewById(R.id.seller_login_password);
        loadingbar = new ProgressDialog(this);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }
    private void loginUser() {
        String phone = phoneNumber.getText().toString();
        String paswd = password.getText().toString();
        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Enter your phone number", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(paswd))
        {
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Login Account");
            loadingbar.setMessage("Please wait, while we are checking the credential");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            AllowAccess(phone,paswd);
        }
    }

    private void AllowAccess(final String phone, final String paswd) {

     /*   if(checkBoxremberme.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,paswd);
        }*/
        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(paswd))
                        {

                                Toast.makeText(SellerLoginActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(SellerLoginActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }

                }
                else {
                    Toast.makeText(SellerLoginActivity.this, "Account Not Exist." , Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
