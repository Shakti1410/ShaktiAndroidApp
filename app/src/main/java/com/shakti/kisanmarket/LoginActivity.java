package com.shakti.kisanmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.shakti.kisanmarket.Admin.AdminHomeActivity;
import com.shakti.kisanmarket.Sellers.SellerProductCategoryActivity;
import com.shakti.kisanmarket.Model.Users;
import com.shakti.kisanmarket.Prevalent.Prevalent;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText phoneNumber,password;
    Button login_btn;
    private ProgressDialog loadingbar;
    private String parentDbName = "Users";
    private CheckBox checkBoxremberme;
    private TextView admin_link,not_admin,forget_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = (Button)findViewById(R.id.login_btn);
        phoneNumber = (EditText) findViewById(R.id.login_phone);
        password = (EditText) findViewById(R.id.login_password);
        admin_link = (TextView) findViewById(R.id.admin_panel_link);
        forget_password = (TextView) findViewById(R.id.forget_password_link);
        not_admin = (TextView) findViewById(R.id.not_admin_panel_link);
        checkBoxremberme = (CheckBox) findViewById(R.id.remeber_me_chk);
        loadingbar = new ProgressDialog(this);
        Paper.init(this);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        admin_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_btn.setText("Login Admin");
                admin_link.setVisibility(View.INVISIBLE);
                not_admin.setVisibility(View.VISIBLE);
                parentDbName ="Admins";
            }
        });
        not_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_btn.setText("Login");
                admin_link.setVisibility(View.VISIBLE);
                not_admin.setVisibility(View.INVISIBLE);
                parentDbName ="Users";

            }
        });
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
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

        if(checkBoxremberme.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,paswd);
        }
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
                           if(parentDbName.equals("Admins"))
                           {
                               Toast.makeText(LoginActivity.this, "Welcome Admin, You are Logged in Successfully...", Toast.LENGTH_SHORT).show();
                               loadingbar.dismiss();
                               Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                               startActivity(intent);
                           }
                           else if(parentDbName.equals("Users"))
                           {
                               Toast.makeText(LoginActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                               loadingbar.dismiss();
                               Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                               Prevalent.currentOnlineUser = usersData;
                               startActivity(intent);
                           }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }

                }
                else {
                    Toast.makeText(LoginActivity.this, "Account Not Exist." , Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
