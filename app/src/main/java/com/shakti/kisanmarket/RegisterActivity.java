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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Button creat_account_btn;
    private EditText inputName,phone_num,password;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        creat_account_btn = (Button)findViewById(R.id.register_btn);
        inputName = (EditText) findViewById(R.id.register_name);
        phone_num = (EditText) findViewById(R.id.register_phone);
        password = (EditText) findViewById(R.id.register_password);
        loadingbar = new ProgressDialog(this);
        
        creat_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name = inputName.getText().toString();
        String phone = phone_num.getText().toString();
        String paswd = password.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please Write your Name", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Enter your phone number", Toast.LENGTH_SHORT).show();
        }
        else if(!isValidPhone(phone) )
        {
            Toast.makeText(this, "Please Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        else  if(TextUtils.isEmpty(paswd))
        {
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please wait, while we are checking the credential");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            Validatephone(name,phone,paswd);
        }
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
    private void Validatephone(final String name, final String phone, final String paswd) {

        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> userdataMap =new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",paswd);
                    userdataMap.put("name",name);
                    rootref.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Congratulation, your account has been created." , Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Network Error, please try again." , Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This" + phone + "already exist." , Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Try again using another Phone Number." , Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
