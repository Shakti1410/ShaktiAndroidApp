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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shakti.kisanmarket.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameedt,phoneedt,addressedt,cityedt;
    private Button confirmOrder_btn;

    private String totalamnt= "", key ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalamnt = getIntent().getStringExtra("Total Price");
        confirmOrder_btn =(Button)findViewById(R.id.confirm_order_btn);
        nameedt = (EditText)findViewById(R.id.shipment_name);
        phoneedt =  (EditText)findViewById(R.id.shipment_phone);
        addressedt = (EditText)findViewById(R.id.shipment_address);
        cityedt = (EditText)findViewById(R.id.shipment_city);

        confirmOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
    }

    private void CheckData() {

        if(TextUtils.isEmpty(nameedt.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(phoneedt.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if(!isValidPhone(phoneedt.getText().toString()) )
        {
            Toast.makeText(this, "Please Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
       else if(TextUtils.isEmpty(addressedt.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(cityedt.getText().toString()))
        {
            Toast.makeText(this, "Please Enter City", Toast.LENGTH_SHORT).show();
        }
       else {
           confirmOrder();
        }
    }

    private void confirmOrder()
    {
       final String saveCurrentTime,saveCurrentDate;
        Calendar calFordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calFordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calFordate.getTime());
        final DatabaseReference orderhistRef = FirebaseDatabase.getInstance().getReference().child("Orders History")
                .child(Prevalent.currentOnlineUser.getPhone()+saveCurrentDate+saveCurrentTime);
        final DatabaseReference carthistoryRef = FirebaseDatabase.getInstance().getReference().child("Cart History");
        key = carthistoryRef.push().getKey();
        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        final HashMap<String,Object> orderMap = new HashMap<>();
        orderMap.put("totalAmount",totalamnt);
        orderMap.put("name",nameedt.getText().toString());
        orderMap.put("phone",phoneedt.getText().toString());
        orderMap.put("address",addressedt.getText().toString());
        orderMap.put("city",cityedt.getText().toString());
        orderMap.put("date",saveCurrentDate);
        orderMap.put("pid",saveCurrentDate+saveCurrentTime);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("state", "not Shipped");
        orderMap.put("key", key);
        orderMap.put("id", Prevalent.currentOnlineUser.getPhone());
        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                   orderhistRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                                   if (task.isSuccessful()) {

                                     //  Toast.makeText(ConfirmFinalOrderActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(ConfirmFinalOrderActivity.this,FinalOrderActivity.class);
                                    //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       intent.putExtra("key",key);
                                       intent.putExtra("pid",saveCurrentDate+saveCurrentTime);
                                       startActivity(intent);

                                   }

                       }
                   });
                }
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
