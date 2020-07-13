package com.shakti.kisanmarket.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.shakti.kisanmarket.LoginActivity;
import com.shakti.kisanmarket.MainActivity;
import com.shakti.kisanmarket.R;
import com.shakti.kisanmarket.RegisterActivity;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button alredy_account_btn,register_btn;
    private EditText nameInput,phoneInput,passwordInput,addressInput;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        alredy_account_btn = (Button)findViewById(R.id.alredy_have_account);
        register_btn = (Button)findViewById(R.id.seller_reg_btn);
        nameInput = (EditText) findViewById(R.id.seller_name);
        phoneInput = (EditText) findViewById(R.id.seller_phone);
        passwordInput = (EditText) findViewById(R.id.seller_password);
        addressInput = (EditText) findViewById(R.id.seller_address);
        loadingbar = new ProgressDialog(this);

        alredy_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerseller();
            }
        });
    }

    private void registerseller()
    {
        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();
        String address = addressInput.getText().toString();

        if(!name.equals("") && !phone.equals("") && !password.equals("") && !address.equals("") &&isValidPhone(phone))
        {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please wait, while we are checking the credential");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            Validatephone(name,phone,password,address);
        }

        else
        {
            Toast.makeText(this, "Please Complete Registration Form", Toast.LENGTH_SHORT).show();
        }
    }

    private void Validatephone(final String name, final String phone, final String password, final String address)
    {
        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Seller").child(phone).exists()))
                {
                    HashMap<String,Object> userdataMap =new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);
                    userdataMap.put("address",address);
                    rootref.child("Seller").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(SellerRegistrationActivity.this, "Congratulation, your account has been created." , Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(SellerRegistrationActivity.this, "Network Error, please try again." , Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(SellerRegistrationActivity.this, "This" + phone + "already exist." , Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(SellerRegistrationActivity.this, "Try again using another Phone Number." , Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SellerRegistrationActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public static boolean isValidPhone(String phone)
    {
        String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{9,12}$";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
        }
    }
}
